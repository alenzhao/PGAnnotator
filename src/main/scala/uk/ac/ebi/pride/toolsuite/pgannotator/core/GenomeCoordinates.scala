package uk.ac.ebi.pride.toolsuite.pgannotator.core

/**
  * Created by yperez on 22/08/2016.
  */

class Coordinates(var start:Int, var end:Int, var nTerm: Offset.Offset, var cTerm:Offset.Offset){

  def this() = this(0, 0, Offset.DEFAULT, Offset.DEFAULT)

}

class GenomeCoordinates(start:Int, end:Int, nTerm: Offset.Offset, cTerm:Offset.Offset, var chromosome: Chromosome.Chromosome, var scaffoldChr:String, var strand:Strand.Strand, var frame:Frame.Frame)
  extends Coordinates(start, end, nTerm, cTerm){

  def this() = this(0, 0, Offset.DEFAULT, Offset.DEFAULT, Chromosome.SCAFFOLD, "", Strand.UNKNOWN, Frame.UNKNOWN)

}







