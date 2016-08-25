package uk.ac.ebi.pride.toolsuite.pgannotator.core

import uk.ac.ebi.pride.toolsuite.pgannotator.utils.Constants

/**
  * This class contain the information around a ENSEMBL Protein Entry in fasta files. Each protein entry contains the Protein accession, Gene Accession
  * Transcript Accession, and protein coordinates.
  *
  * @author ypriverol
  * */

class EnsemblProteinEntry(header:String, sequence: String) extends FastaEntry(header:String, sequence:String){

  val listHeader = header.split("\\s+")
  var accession: String = if(accession == null && listHeader.nonEmpty) listHeader(Constants.ENSEMBL_ACCESSION_POSITION) else accession
  var geneAccession:String = if(geneAccession == null && listHeader.size > 3) listHeader(Constants.ENSEMBL_GENE_ACCESSION_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(1) else geneAccession
  var transcriptAccession:String = if(transcriptAccession == null && listHeader.size > 4) listHeader(Constants.ENSEMBL_TRANSCRIPT_ACCESSION_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(1)  else transcriptAccession
  var chromosome:String = if(chromosome == null && listHeader.size > 2) listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(2) else chromosome
  var startPos:String = if(startPos == null && listHeader.size > 2) listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(3) else startPos
  var endPost:String = if(endPost == null && listHeader.size > 2) listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(4) else endPost
  var trans:String = if(trans == null && listHeader.size > 2) listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(5)  else trans
  val isoSequence:String = sequence.replaceAll("L", "J").replaceAll("I", "J")

}
