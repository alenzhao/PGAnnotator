package uk.ac.ebi.pride.toolsuite.pgannotator.utils

/**
  * General Constants use in CordAnnotator.
  */
object Constants {

  // Constants to parse a EMSEMBL Protein Entry from a FASTA Entry.
  val ENSEMBL_ACCESSION_POSITION                 = 0
  val ENSEMBL_GENE_ACCESSION_POSITION            = 3
  val ENSEMBL_TRANSCRIPT_ACCESSION_POSITION      = 4
  val ENSEMBL_ACCESSION_SEPARATOR                = ":"
  val ENSEMBL_CHROMOSOME_POSITION                = 2

  //GTF Constants Properties
  val GTF_TRANSCRIPT_IDENTIFIER_ATTRIBUTE        = "transcript_id"
  val GTF_TRANSCRIPT_HAVANA_IDENTIFIER_ATTRIBUTE = "havana_transcript"
  val GTF_GENE_IDENTIFIER_ATTRIBUTE              = "gene_id"
  val GTF_GENE_HAVANA_IDENTIFIER_ATTRIBUTE       = "havana_gene"
  val GTF_GENE_NAME_ATTRIBUTE                    = "gene_name"
  val GTF_GENE_PROPERTY                          = "gene"
  val GTF_TRANSCRIPT_PROPERTY                    = "transcript"
  val GTF_LINE_PROPERTY_SEP                      = "\t"
  val GTF_ATTRIBUTE_SEP                          = ";"
  val GTF_ATTRIBUTE_VALUE_SEP                    = "\""
  val GTF_CDS_PROPERTY                           = "CDS"
  val GTF_GENE_TYPE_ATTRIBUTE                    = "gene_type"
  val GTF_GENE_STATUS_ATTRIBUTE                  = "gene_status"



}