package uk.ac.ebi.pride.toolsuite.pgannotator.utils

/**
  * Different Utilities to parse Numbers or String processing. These are Generic functions used by different classes and objects
  * int CordAnnotator
  */

object Utils {
  def isNumeric(input: String): Boolean = input.forall(_.isDigit)
}
