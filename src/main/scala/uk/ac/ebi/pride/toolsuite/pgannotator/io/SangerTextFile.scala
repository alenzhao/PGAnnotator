package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.File

import uk.ac.ebi.pride.toolsuite.pgannotator.core.{SangerTextEntry, SangerTextPeptideSequence}
import uk.ac.ebi.pride.toolsuite.pgannotator.exceptions.CordAnnotatorException
import uk.ac.ebi.pride.toolsuite.pgannotator.utils.{Constants, IOUtils}


/**
  * This file is produced by Sanger Proteomics team (Wright, James C., et al. "Improving GENCODE reference gene annotation using a high-stringency proteogenomics workflow."
  * Nature communications 7 (2016). -http://www.nature.com/ncomms/2016/160602/ncomms11778/full/ncomms11778.html -).
  * The sanger file is a tab delimited file with the following fields:
  * Experiment :       Name of the Experiment
  * Type:              Type of the Experiment
  * Peptide:           Peptide Sequence including the modifications in the current structure AA(Modification)
  * PSMs:              Number of PSMs for the following peptide.
  * sigPSMs_A:         Significant PSMs following first criteria A
  * sigPSMs_B:         Significant PSMs following second criteria B
  * sigPSMs_C:         Significant PSMs following third criteria  C
  * PEP:               Posterior error probability of the peptide 1% PSM FDR and 0.05 PEP.
  * PEP_B:             Posterior error probability of the peptide 1%FDR and 0.01 PEP
  * PEP_C:             Posterior error probability of the peptide 1%FDR and 0.01 PEP peptide length between 7 and 30 residues
  * SE:
  * Decoy:             0 1 based system to say if the peptide is a decoy or a target peptide
  * #Genes:            Number of genes
  * Genes:             Genes Identifiers separated by ,
  * #Transcripts:      Number of Transcripts
  * Transcripts:       Transcripts Identifiers separated by ,
  * PrecursorIBAQ:     Precursor IBAQ
  * FragIBAQ:          Fragment IBAQ
  */
class SangerTextFile(file: File) {

  @throws(classOf[CordAnnotatorException])
  def parseOneGTFEntry(textLine:String): (SangerTextEntry) = {

    val sangerTextLine = textLine.split(Constants.GENERAL_LINE_TAB_PROPERTY_SEP)
    if(sangerTextLine.length != 18)
         throw CordAnnotatorException("The number of columns is incorrect for this line: " + textLine)
    println(textLine)
    SangerTextEntry(sangerTextLine(0), sangerTextLine(1), new SangerTextPeptideSequence(sangerTextLine(2)), sangerTextLine(3).toInt, sangerTextLine(4).toInt,
      sangerTextLine(5).toInt, sangerTextLine(6).toInt, sangerTextLine(7).toDouble, sangerTextLine(8).toDouble, sangerTextLine(9).toDouble, sangerTextLine(10).toInt,
      sangerTextLine(11).toInt, sangerTextLine(12).toInt, sangerTextLine(13).split(Constants.GENERAL_LINE_COMMA_PROPERTY_SEP).toList, sangerTextLine(14).toInt,
      sangerTextLine(15).split(Constants.GENERAL_LINE_COMMA_PROPERTY_SEP).toList, sangerTextLine(16).toDouble, sangerTextLine(17).toDouble)
  }

  /**
  * Parse the given source and produce an iterator of FastaEntry. It returns an
  * Iterator of FastaEntry.
  *
  * @return An Iterator of FastaEntry Iterator[FastaEntry]
  */

  def parseGzip: Iterator[SangerTextEntry] = {
    val lines = IOUtils.inputSource(file, gzip = true).getLines()
    if(lines.hasNext) lines.next()   // Skip first line because is the header
    val it: Iterator[String] = new Iterator[String] {
      override def hasNext: Boolean = lines.hasNext
      override def next(): String = lines.next()
    }
    it.toStream.par.map(parseOneGTFEntry).iterator
  }

  def parse: Iterator[SangerTextEntry] = {
    val lines = IOUtils.inputSource(file, gzip = false).getLines()
    if(lines.hasNext) lines.next() // Skip first line because is the header
    val it: Iterator[String] = new Iterator[String] {
      override def hasNext: Boolean = lines.hasNext
      override def next(): String = lines.next()
    }
    it.toStream.par.map(parseOneGTFEntry).iterator
  }
}

/**
  * Companion GTFParser Object
  */
object SangerTextFile {

  def apply(filename: String) = new SangerTextFile(new File(filename))

  def apply(file: File) = new SangerTextFile(file)
}
