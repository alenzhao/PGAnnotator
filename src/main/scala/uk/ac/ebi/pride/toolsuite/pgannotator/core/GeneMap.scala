package uk.ac.ebi.pride.toolsuite.pgannotator.core

import scala.collection.immutable.SortedMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by yperez on 08/09/2016.
  */
class GeneMap(){

  def addTranscriptID(gtfEntry: GTFEntry) = {
    val geneEntryMap = map.getOrElse(gtfEntry.geneIdentifier, null)
    if(geneEntryMap != null) geneEntryMap.addTranscript(gtfEntry.transcriptIdentifier)
  }

  val map: mutable.Map[String, GeneEntryMap] = mutable.Map[String, GeneEntryMap]()

  def sortedMap: SortedMap[String, GeneEntryMap] = SortedMap(map.toList:_*)

  var tissueMap: Map[String, Int]        = null

  var tissueIndex                        = 0

  var peptideCount                       = 0

  def addGene(gtfGeneEntry:GTFGeneEntry, transcripts: List[String]): Unit = map += gtfGeneEntry.id -> new GeneEntryMap(gtfGeneEntry, transcripts.to[ListBuffer])

  def addGene(gTFGeneEntry: GTFGeneEntry): Unit = map += gTFGeneEntry.id -> new GeneEntryMap(gTFGeneEntry)

}

class GeneEntryMap(val gTFGeneEntry: GTFGeneEntry, val transcripts: ListBuffer[String],
                   var peptideEntries: mutable.Map[String,PeptideEntry]){

  def addTranscript(transcriptIdentifier: String) = transcripts += transcriptIdentifier

  def sortedTranscripts: ListBuffer[String] = transcripts.sorted

  def this(gTFGeneEntry: GTFGeneEntry, transcripts:ListBuffer[String] ){
    this(gTFGeneEntry, transcripts, mutable.Map[String,PeptideEntry]())
  }

  def this(gTFGeneEntry: GTFGeneEntry){
    this(gTFGeneEntry, ListBuffer[String](), mutable.Map[String, PeptideEntry]())
  }

}