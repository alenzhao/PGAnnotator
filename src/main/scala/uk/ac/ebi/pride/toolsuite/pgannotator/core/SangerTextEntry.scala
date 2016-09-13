package uk.ac.ebi.pride.toolsuite.pgannotator.core

import java.util.regex.Pattern

import uk.ac.ebi.pride.toolsuite.pgannotator.utils.Constants

import scala.collection.mutable.ListBuffer

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

case class SangerTextEntry (experiment: String, typeExperiment: String, peptide: SangerTextPeptideSequence, nPSM: Int, nsigPSMA: Int,
                       nsigPSMB: Int, nsigPSMC: Int, pep: Double, pepB: Double, pepC: Double, se: Int, decoy: Int,
                       nGenes: Int, genes: List[String], nTranscripts: Int, transcripts: List[String], precursorIBAQ: Double, fragmentIBAQ:Double)

class SangerTextPeptideSequence(var sequence:String, var modifications: List[Tuple2[String, Int]]){

  def this( stringSequence: String) = {
    this(null, null)
    if(stringSequence.contains("("))
      println(stringSequence)
    val parts:List[String] = stringSequence.split(Constants.SangerPTMPattern).toList
    this.sequence = parts.zipWithIndex.filter(_._2 % 2 == 0).map(_._1).toList.mkString("")
    this.modifications = if(parts.length > 1) parts.grouped(2).collect { case List(toIndex, value) => (value, toIndex.length)}.toList else List()
  }
}
