package uk.ac.ebi.pride.toolsuite.pgannotator.core

import uk.ac.ebi.pride.toolsuite.pgannotator.utils.Constants

import scala.collection.mutable.ListBuffer

/**
  * This class contain the information around a ENSEMBL Protein Entry in fasta files. Each protein entry contains the Protein accession, Gene Accession
  * Transcript Accession, and protein coordinates.
  *
  * @author ypriverol
  * */

class EnsemblProteinEntry(header:String, sequence: String, val genomeCoordinates: GenomeCoordinates, var multiMapCoordinates: List[Tuple2[Coordinates, GenomeCoordinates]])

  extends FastaEntry(header:String, sequence:String){

  /**
    * This constructor creates a default Ensembl Protein
    */
  def this() = { this("", "", new GenomeCoordinates(), List[Tuple2[Coordinates, GenomeCoordinates]]())}

  /**
    * Create an Ensembl protein entry from the Fasta Entry.
    * @param header
    * @param sequence
    */
  def this(header: String, sequence: String){
    this(header, sequence, new GenomeCoordinates(), List[Tuple2[Coordinates, GenomeCoordinates]]())
  }

  val listHeader = header.split("\\s+")

  var accession: String = if(accession == null && listHeader.nonEmpty)
    listHeader(Constants.ENSEMBL_ACCESSION_POSITION) else accession

  var geneAccession:String = if(geneAccession == null && listHeader.size > 3)
    listHeader(Constants.ENSEMBL_GENE_ACCESSION_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(1).split("\\.")(0) else geneAccession

  var transcriptAccession:String = if(transcriptAccession == null && listHeader.size > 4){
    if(listHeader(Constants.ENSEMBL_TRANSCRIPT_ACCESSION_POSITION).contains("ENST00000456328"))
      println(listHeader(Constants.ENSEMBL_TRANSCRIPT_ACCESSION_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(1).split("\\.")(0))
    listHeader(Constants.ENSEMBL_TRANSCRIPT_ACCESSION_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(1).split("\\.")(0)
  } else transcriptAccession

  var chromosome:String = if(chromosome == null && listHeader.size > 2)
    listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(2) else chromosome

  var startPos:String = if(startPos == null && listHeader.size > 2)
    listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(3) else startPos

  var endPost:String = if(endPost == null && listHeader.size > 2)
    listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(4) else endPost

  var trans:String = if(trans == null && listHeader.size > 2)
    listHeader(Constants.ENSEMBL_CHROMOSOME_POSITION).split(Constants.ENSEMBL_ACCESSION_SEPARATOR)(5)  else trans

  val isoSequence:String = sequence.replaceAll("L", "J").replaceAll("I", "J")

  def multiMapCoordinates(map: ListBuffer[Tuple2[Coordinates, GenomeCoordinates]]): Unit = {
    multiMapCoordinates = map.sortWith{ case ((n1, m1), (n2, m2))  => (n1.start < n2.start && n1.end < n2.end && n1.end < n2.start)}.toList
  }

}