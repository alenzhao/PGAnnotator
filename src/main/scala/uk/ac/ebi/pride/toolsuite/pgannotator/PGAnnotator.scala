package uk.ac.ebi.pride.toolsuite.pgannotator

import java.io.File

import org.kohsuke.args4j.{CmdLineException, CmdLineParser, Option}
import uk.ac.ebi.pride.toolsuite.pgannotator.core._
import uk.ac.ebi.pride.toolsuite.pgannotator.core.GTFGeneEntry
import uk.ac.ebi.pride.toolsuite.pgannotator.io.{FastaParser, GTFParser}
import uk.ac.ebi.pride.toolsuite.pgannotator.utils.IOUtils

import scala.collection.immutable.SortedMap
import scala.collection.mutable.ListBuffer
/**
  * Main class to do Gene Coordinates mapping from the Text, Csv, or mzTab file into
  * Gene Coordinates.
  *
  * @author ypriverol
  */
object PGAnnotator{

  class PGOptions(){

    @Option(name = "-fasta", usage = "ENSEMBL fasta file to be use during the annotation", required = true)
    var fastaFile: String = _

    @Option(name = "-gtf", usage = "ENSEMBL GTF file to be use to get the chromosome coordinates", required = true)
    var gtf: String = _

    @Option(name ="--help", aliases=Array("-h"), usage="Show this message")
    var help = false
  }

  def main(args: Array[String]): Unit ={

    val options: PGOptions = new PGOptions
    val parser:CmdLineParser = new CmdLineParser(options)

    try{

      parser.parseArgument(args:_*)

      val fastaEntriesUnsorted = (if(IOUtils.isGZip(new File(options.fastaFile))) FastaParser(new File(options.fastaFile)).parseGzip.toList
                            else FastaParser(new File(options.fastaFile)).parse.toList).par.map(o => new EnsemblProteinEntry(o.header, o.sequence)).toList.par map (t => t.transcriptAccession -> t) toMap

      val fastaEntries = SortedMap(fastaEntriesUnsorted.toList:_*)

      val gtfEntries:List[GTFEntry]     = if(IOUtils.isGZip(new File(options.gtf))) GTFParser(new File(options.gtf)).parseGzip.toList
                                                 else GTFParser(new File(options.gtf)).parse.toList

      val geneMap:Map[String, GTFGeneEntry] = gtfEntries.toStream.par.filter(_.isGene).map(f => f.geneIdentifier -> GTFGeneEntry(f))(collection.breakOut)

      val mapping = new GeneMap

      var protCoord:Coordinates     = new Coordinates()
      var prevprotCoord:Coordinates = new Coordinates()
      var coordMap = ListBuffer[Tuple2[Coordinates, GenomeCoordinates]]()
      var protein:EnsemblProteinEntry = null
      var codingLength = 0

      for (gtfEntry <- gtfEntries.toStream) {
        if (gtfEntry.isGene)
          mapping.addGene(GTFGeneEntry(gtfEntry))

        if(gtfEntry.isTranscript){

          mapping.addTranscriptID(gtfEntry)

          if(protein != null) protein.multiMapCoordinates(coordMap)

          protein = fastaEntries.getOrElse(gtfEntry.transcriptIdentifier, null)
          if(protein == null)
            protein = new EnsemblProteinEntry()

          protCoord = new Coordinates()
          prevprotCoord = new Coordinates()
          prevprotCoord.cTerm = Offset.OFFSET_3
          prevprotCoord.nTerm = Offset.OFFSET_3
          coordMap = ListBuffer[Tuple2[Coordinates, GenomeCoordinates]]()
          codingLength = 0

        } else if(gtfEntry.isCDS){

          val genCoord:GenomeCoordinates = GTFGeneEntry(gtfEntry).genomeCoordinates
          protCoord = new Coordinates()

          // get nterm from prev exon

          if(genCoord.frame!=Frame.UNKNOWN)

            protCoord.nTerm=(Offset.getOffSetByValue(genCoord.frame.nName))

          else {

            if(prevprotCoord.cTerm!=Offset.OFFSET_3)
              protCoord.nTerm=Offset.getOffSetByValue((3-prevprotCoord.cTerm.nName))
            else
              protCoord.nTerm=Offset.OFFSET_3
          }
          var length = 0

          if (gtfEntry.isFirstStrand)
            length = genCoord.end - genCoord.start + 1
          else if(!gtfEntry.isFirstStrand)
            length = genCoord.start - genCoord.end + 1

          codingLength = codingLength + length
          if(length % 3 == 0) {
            if(protCoord.nTerm != Offset.OFFSET_3) protCoord.cTerm= Offset(3-protCoord.nTerm.nName)
            else
              protCoord.cTerm = Offset.OFFSET_3
          } else if(length % 3 == 2) {
            if(protCoord.nTerm == Offset.OFFSET_3)
              protCoord.cTerm = Offset.OFFSET_2
            else if(protCoord.nTerm == Offset.OFFSET_2)
              protCoord.cTerm = Offset.OFFSET_3
            else if(protCoord.nTerm == Offset.OFFSET_1)
              protCoord.cTerm = Offset.OFFSET_1
          } else if(length % 3 == 1) {
            if(protCoord.nTerm == Offset.OFFSET_3)
              protCoord.cTerm = Offset.OFFSET_1
            else if(protCoord.nTerm == Offset.OFFSET_1)
              protCoord.cTerm = Offset.OFFSET_3
            else if(protCoord.nTerm == Offset.OFFSET_2)
              protCoord.cTerm = Offset.OFFSET_2
          }
          // calc protein coordinates
          if(protCoord.nTerm != Offset.OFFSET_3)
            protCoord.start=prevprotCoord.end
          else{
            if(prevprotCoord.end==0 && coordMap.isEmpty)
              protCoord.start = 0
            else
              protCoord.start = prevprotCoord.end + 1
          }

          var offsets = 0
          if(protCoord.nTerm != Offset.OFFSET_3)
             offsets += protCoord.nTerm.nName

          if (gtfEntry.isFirstStrand)
            length = genCoord.end - genCoord.start + 1 - offsets
          else if(!gtfEntry.isFirstStrand)
            length = genCoord.start - genCoord.end + 1 - offsets

          val peplength = length / 3

          var pepend = protCoord.start + peplength - 1
          if(protCoord.cTerm != Offset.OFFSET_3)
            pepend = pepend + 1
          if(protCoord.nTerm != Offset.OFFSET_3)
            pepend = pepend + 1

          protCoord.end = pepend

          prevprotCoord = protCoord

          coordMap +=  ((protCoord,genCoord))
        }
      }
      if(protein!=null) {
        protein.multiMapCoordinates(coordMap)
      }

      val map = mapping.sortedMap

      if (options.help) {
        parser.printUsage(System.err)
        sys.exit(0)
      }
    } catch {

      case e: CmdLineException =>
        print(s"Error:${e.getMessage}\n Usage: \n")
        parser.printUsage(System.out)
        System.out.println(1)
    }
       }


}


