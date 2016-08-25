package uk.ac.ebi.pride.toolsuite.pgannotator.core

import com.sun.org.apache.xalan.internal.xsltc.compiler


/**
  * The GTFGene Entry contains the information of a Gene in ENSEMBL genomes and only handle the information
  * of Genome Coordinates.
  */

class GTFGeneEntry (chromosome:Chromosome.Chromosome, source: String, feature:String, start:Int, end:Int,
                         score:Double, strand: Strand.Strand, frame: Frame.Frame, attributes: String) extends
  GTFEntry(chromosome:Chromosome.Chromosome, source: String, feature:String, start:Int, end:Int,
  score:Double, strand: Strand.Strand, frame: Frame.Frame, attributes: String){

  def this(gTFEntry: GTFEntry) = this(gTFEntry.chromosome, gTFEntry.source, gTFEntry.feature, gTFEntry.start, gTFEntry.end, gTFEntry.score, gTFEntry.strand, gTFEntry.frame, gTFEntry.attributes)

  def genomeCoordinates: GenomeCoordinates = {
    val chrScaffold = if(chromosome == Chromosome.SCAFFOLD) "0" else compiler.Constants.EMPTYSTRING
    var cStart: Int = 0
    var cEnd: Int   = 0
    if(strand == Strand.FORWARD){
      cStart = start
      cEnd = end
    }else if(strand == Strand.REVERSE){
      cStart = end
      cEnd = start
    }
    GenomeCoordinates(cStart, cEnd, Offset.DEFAULT, Offset.DEFAULT, chromosome, chrScaffold, strand, frame)
  }

}



