package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.File

import org.scalatest.concurrent.ScalaFutures
import org.specs2.mutable.Specification

/**
  * Created by yperez on 12/09/2016.
  */
class SangerTextFileTest extends Specification with ScalaFutures{

  "Fasta File Parsed : " should {
    val url = SangerTextFileTest.this.getClass.getClassLoader.getResource("TestPeptides.txt")

    if(url == null)
      throw new IllegalStateException("No file for input found!!")

    val t1 = System.currentTimeMillis
    val fNG = SangerTextFile(new File(url.toURI).getAbsolutePath).parse.toList

    "numberOfEntries should be 19" in {
      fNG.size must equalTo(19)
    }

    println("Object Parser: " + ((System.currentTimeMillis - t1)/1000).toString)

  }
}