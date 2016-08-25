package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.File

import uk.ac.ebi.pride.toolsuite.pgannotator.core.{Chromosome, Frame, GTFEntry, Strand}
import uk.ac.ebi.pride.toolsuite.pgannotator.exceptions.CordAnnotatorException
import uk.ac.ebi.pride.toolsuite.pgannotator.utils.{Constants, IOUtils}


/**
  * Reading a GFT file from EMSMBL (http://www.ensembl.org/info/website/upload/gff.html). The GTF file contains
  * the information about the transcript, gene and gene coordinates for every chromosome in ENSEMBL. The GTF parser
  * reade the plain file and map the information into a GTFEntry.
  *
  * @author ypriverol
  *
  */
class GTFParser(file: File) {

  @throws(classOf[CordAnnotatorException])
  def parseOneGTFEntry(gtfLine:String): (GTFEntry) = {
    val gtfLineArr = gtfLine.split(Constants.GTF_LINE_PROPERTY_SEP)
    if(gtfLineArr.length != 9)
      throw CordAnnotatorException("The number of columns is incorrect for this line: " + gtfLine)
    GTFEntry(Chromosome.getChromosomeByName(gtfLineArr(0)), gtfLineArr(1), gtfLineArr(2), gtfLineArr(3).toInt, gtfLineArr(4).toInt,
      processNull(gtfLineArr(5)), Strand.getStrandByName(gtfLineArr(6)), Frame.getFrameByName(gtfLineArr(7)),gtfLineArr(8))
  }

  /**
    * Parse the given source and produce an iterator of FastaEntry. It returns an
    * Iterator of FastaEntry.
    *
    * @return An Iterator of FastaEntry Iterator[FastaEntry]
    */
  def parseGzip: Iterator[GTFEntry] = {
    val lines = IOUtils.inputSource(file, gzip = true).getLines()
    while(lines.hasNext && lines.next.startsWith("#")){}
    val it: Iterator[String] = new Iterator[String] {
      override def hasNext: Boolean = lines.hasNext
      override def next(): String = lines.next()
    }
    it.toStream.par.map(parseOneGTFEntry).iterator
  }

  def parse: Iterator[GTFEntry] = {
    val lines = IOUtils.inputSource(file, gzip = false).getLines()
    while(lines.hasNext && lines.next.startsWith("#")){}
    val it: Iterator[String] = new Iterator[String] {
      override def hasNext: Boolean = lines.hasNext
      override def next(): String = lines.next()
    }
    it.toStream.par.map(parseOneGTFEntry).iterator
  }

  def processNull(stringLine: String): Double = if(stringLine.equalsIgnoreCase(".")) 0.0 else stringLine.toDouble

}

/**
  * Companion GTFParser Object
  */
object GTFParser {

  def apply(filename: String) = new GTFParser(new File(filename))

  def apply(file: File) = new GTFParser(file)

}
