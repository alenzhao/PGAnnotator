package uk.ac.ebi.pride.toolsuite.pgannotator

import java.io.File

import org.kohsuke.args4j.{CmdLineException, CmdLineParser, Option}
import uk.ac.ebi.pride.toolsuite.pgannotator.core.{EnsemblProteinEntry, GTFGeneEntry, PeptideEntry, GTFEntry}
import uk.ac.ebi.pride.toolsuite.pgannotator.io.{FastaParser, GTFParser}
import uk.ac.ebi.pride.toolsuite.pgannotator.utils.IOUtils

import scala.collection.mutable
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

      val fastaEntries = (if(IOUtils.isGZip(new File(options.fastaFile))) FastaParser(new File(options.fastaFile)).parseGzip.toList
                            else FastaParser(new File(options.fastaFile)).parse.toList).par.map(o => new EnsemblProteinEntry(o.header, o.sequence)).toList.par map (t => t.transcriptAccession -> t) toMap

      val gtfEntries:List[GTFEntry]     = if(IOUtils.isGZip(new File(options.gtf))) GTFParser(new File(options.gtf)).parseGzip.toList
                                                 else GTFParser(new File(options.gtf)).parse.toList

      val geneMap:Map[String, GTFGeneEntry] = gtfEntries.toStream.par.filter(_.isGene).map(f => f.geneIdentifier -> new GTFGeneEntry(f))(collection.breakOut)

      val mapping = new GeneMap

      for (gtfEntry <- gtfEntries.toStream) {
        if (gtfEntry.isGene)
          mapping.addGene(new GTFGeneEntry(gtfEntry))
        if(gtfEntry.isTranscript){
          mapping.addTranscriptID(gtfEntry)
          val protein = fastaEntries.toStream.par.filter(_._2.transcriptAccession.equalsIgnoreCase(gtfEntry.transcriptIdentifier)).toList
          print(protein.size)
        }
      }

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

class GeneMap(){
  def addTranscriptID(gtfEntry: GTFEntry) = {
    val geneEntryMap = mapping.getOrElse(gtfEntry.geneIdentifier, null)
    if(geneEntryMap != null) geneEntryMap.addTranscript(gtfEntry.transcriptIdentifier)
  }

  var mapping: mutable.Map[String, GeneEntryMap] = mutable.Map[String, GeneEntryMap]()
  var tissueMap: Map[String, Int]        = null
  var tissueIndex                        = 0
  var peptideCount                       = 0

  def addGene(gtfGeneEntry:GTFGeneEntry, transcripts: List[String]): Unit = mapping += gtfGeneEntry.geneIdentifier -> new GeneEntryMap(gtfGeneEntry, transcripts.to[ListBuffer])
  def addGene(gTFGeneEntry: GTFGeneEntry): Unit = mapping += gTFGeneEntry.geneIdentifier -> new GeneEntryMap(gTFGeneEntry)

}

class GeneEntryMap(val gTFGeneEntry: GTFGeneEntry, val transcripts: ListBuffer[String],
                   var peptideEntries: mutable.Map[String,PeptideEntry]){

  def addTranscript(transcriptIdentifier: String) = transcripts += transcriptIdentifier

  def this(gTFGeneEntry: GTFGeneEntry, transcripts:ListBuffer[String] ){
    this(gTFGeneEntry, transcripts, mutable.Map[String,PeptideEntry]())
  }

  def this(gTFGeneEntry: GTFGeneEntry){
    this(gTFGeneEntry, ListBuffer[String](), mutable.Map[String, PeptideEntry]())
  }

}
