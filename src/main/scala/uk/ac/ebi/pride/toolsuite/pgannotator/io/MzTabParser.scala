package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.{File, FileOutputStream, OutputStream}

import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser



/**
  * Created by yperez on 08/09/2016.
  */
class MzTabParser (file: File, mzTabError: OutputStream) extends MZTabFileParser(file: File, mzTabError: OutputStream){
}

/**
  * Companion FastaParser Object
  */
object MzTabParser {

  def apply(filename: String) = new MzTabParser(new File(filename), new FileOutputStream(filename.concat("errors.log")))

  def apply(file: File) = new MzTabParser(file, new FileOutputStream(file.getAbsolutePath.concat("errors.log")))
}
