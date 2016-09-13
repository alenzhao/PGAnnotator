package uk.ac.ebi.pride.toolsuite.pgannotator.core




/**
  * The GTFGene Entry contains the information of a Gene in ENSEMBL genomes and only handle the information
  * of Genome Coordinates.
  */

class GTFGeneEntry (val id: String, val geneType:String, val geneStatus:String, val name:String, val attributes: String, val genomeCoordinates: GenomeCoordinates)

object GTFGeneEntry {
  
  def apply(gTFEntry: GTFEntry): GTFGeneEntry = {
    val chrScaffold = if(gTFEntry.chromosome == Chromosome.SCAFFOLD) "0" else ""
    var cStart: Int = 0
    var cEnd: Int   = 0
    if(gTFEntry.strand == Strand.FORWARD){
      cStart = gTFEntry.start
      cEnd = gTFEntry.end
    }else if(gTFEntry.strand == Strand.REVERSE){
      cStart = gTFEntry.end
      cEnd = gTFEntry.start
    }
    val genomeCoordinates = new GenomeCoordinates(cStart, cEnd, Offset.DEFAULT, Offset.DEFAULT, gTFEntry.chromosome, chrScaffold, gTFEntry.strand, gTFEntry.frame)
    new GTFGeneEntry(gTFEntry.geneIdentifier, gTFEntry.geneType, gTFEntry.geneStatus, gTFEntry.geneName, gTFEntry.attributes, genomeCoordinates)
  }
  
  def apply(id: String, geneType:String,geneStatus:String, name:String, attributes: String, genomeCoordinates: GenomeCoordinates): GTFGeneEntry = new GTFGeneEntry(id, geneType, geneStatus, name, attributes, genomeCoordinates)
  
}