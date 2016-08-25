package uk.ac.ebi.pride.toolsuite.pgannotator.core

/**
  * This class handle only the Fasta Entry information, it is a general class containing
  * the information of the header and String.
  *
  * @author ypriverol
  */

class FastaEntry(val header:String, val sequence:String) {
  var length: Int = sequence.length
}
