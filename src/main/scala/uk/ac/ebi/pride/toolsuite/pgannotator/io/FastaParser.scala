package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.File
import java.util.Scanner

import uk.ac.ebi.pride.toolsuite.pgannotator.core.FastaEntry
import uk.ac.ebi.pride.toolsuite.pgannotator.utils.IOUtils

/**
  * This is a general Fasta File Parse that read every entry and return then into an Iterator Class
  * it important to know that the plain fasta parser only retrieve the FastaEntry that contains a
  * header and a sequence.
  *
  * @author ypriverol
  */

class FastaParser(file: File) {

  def parseOneFastaEntry(protLines: String): FastaEntry = {
    val firstNewLineIndex = protLines.indexOf("\n")
    val header = protLines.substring(0, firstNewLineIndex).replaceAll(">","")
    val seq = protLines.substring(firstNewLineIndex + 1).replaceAll( """\s+""", "")
    new FastaEntry(header, seq)
  }

  /**
    * Parse the given source and produce an iterator of FastaEntry. It returns an
    * Iterator of FastaEntry.
    *
    * @return An iteratior of FastaEntry Iterator[FastaEntry]
    */
  def parse: Iterator[FastaEntry] = {
    val scanner = new Scanner(file).useDelimiter( """\n>""")
    val it: Iterator[String] = new Iterator[String] {
      override def hasNext: Boolean = scanner.hasNext
      override def next(): String = scanner.next()
    }
    it.map(parseOneFastaEntry)
  }

  /**
    * Parse the given source and produce an iterator of FastaEntry. It returns an
    * Iterator of FastaEntry.
    *
    * @return An iteratior of FastaEntry Iterator[FastaEntry]
    */
  def parseGzip: Iterator[FastaEntry] = {
    val scanner = new Scanner(IOUtils.inputStream(file, gzip = true)).useDelimiter( """\n>""")
    val it: Iterator[String] = new Iterator[String] {
      override def hasNext: Boolean = scanner.hasNext
      override def next(): String = scanner.next()
    }
    it.map(parseOneFastaEntry)
  }
}

/**
  * Companion FastaParser Object
  */
object FastaParser {

  def apply(filename: String) = new FastaParser(new File(filename))

  def apply(file: File) = new FastaParser(file)

}



