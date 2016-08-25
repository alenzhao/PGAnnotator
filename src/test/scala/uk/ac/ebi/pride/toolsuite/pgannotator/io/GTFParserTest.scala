package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.File

import org.scalatest.concurrent.ScalaFutures
import org.specs2.mutable.Specification


/**
  * Test all of GTF functionalities
  *
  * @author ypriverol
  */
class GTFParserTest extends Specification with ScalaFutures{

  "Fasta File Parsed : " should {
    val url = GTFParserTest.this.getClass.getClassLoader.getResource("Homo_sapiens.GRCh38.85.gtf.gz")
    if(url == null)
      throw new IllegalStateException("No file for input found!!")

    val t1 = System.currentTimeMillis
    val fNG = GTFParser(new File(url.toURI).getAbsolutePath).parseGzip.toList

    "numberOfEntries should be 2575870" in {
          fNG.size must equalTo(2575870)
    }

    println("Object Parser: " + ((System.currentTimeMillis - t1)/1000).toString)

    "numberOfTranscripts should be 2517820" in {
      fNG.count(_.transcriptIdentifier != null) must equalTo(2517820)
    }

    "numberOfTranscripts should be 2575870" in {
      fNG.count(_.geneIdentifier != null) must equalTo(2575870)
    }

  }


}
