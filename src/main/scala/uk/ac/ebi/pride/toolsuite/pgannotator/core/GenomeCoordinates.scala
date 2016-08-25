package uk.ac.ebi.pride.toolsuite.pgannotator.core

/**
  * Created by yperez on 22/08/2016.
  */

class Coordinates(val start:Int, end:Int, nTerm: Offset.Offset, cTerm:Offset.Offset) extends Comparable[Coordinates]{
  def compareTo(o: Coordinates): Int = start - o.start
}

case class GenomeCoordinates(override val start:Int, end:Int, nTerm: Offset.Offset, cTerm:Offset.Offset, chromosome: Chromosome.Chromosome, scaffoldChr:String, strand:Strand.Strand, frame:Frame.Frame)
  extends Coordinates(start:Int, end:Int, nTerm: Offset.Offset, cTerm:Offset.Offset){

   def compareTo(o: GenomeCoordinates): Boolean = {
    if(chromosome==Chromosome.SCAFFOLD && o.chromosome==Chromosome.SCAFFOLD && scaffoldChr==o.scaffoldChr)
      if(start==o.start) return end < o.end else start<o.start
    else if(chromosome==Chromosome.SCAFFOLD && o.chromosome==Chromosome.SCAFFOLD && scaffoldChr!=o.scaffoldChr) scaffoldChr < o.scaffoldChr
    else if(chromosome==Chromosome.SCAFFOLD && o.chromosome!=Chromosome.SCAFFOLD) false
    else if(chromosome!=Chromosome.SCAFFOLD && o.chromosome==Chromosome.SCAFFOLD) true
    else if(chromosome==o.chromosome){
      if(start==o.start) end < o.end
      else start < o.start
    }
    else
      return chromosome < o.chromosome
  }
}







