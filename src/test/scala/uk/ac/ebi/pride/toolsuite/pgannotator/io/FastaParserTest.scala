package uk.ac.ebi.pride.toolsuite.pgannotator.io

import java.io.File

import org.specs2.mutable.Specification
import org.scalatest.concurrent.ScalaFutures
import uk.ac.ebi.pride.toolsuite.pgannotator.core.EnsemblProteinEntry


/**
  * Read a fasta file an compute the number of entries, an query for a couple of them.
  */
class FastaParserTest extends Specification with ScalaFutures{

  "Fasta File Parsed : " should {
    val url = FastaParserTest.this.getClass.getClassLoader.getResource("Homo_sapiens.GRCh38.pep.all.fa.gz")
    if(url == null)
      throw new IllegalStateException("No file for input found!!")

    val n = FastaParser(new File(url.toURI).getAbsolutePath).parseGzip.toList

    "numberOfEntries should be 102915" in {
      n.size must equalTo(102915)
    }

    val ensembl = n.map(t => new EnsemblProteinEntry(t.header, t.sequence))
    "numberOFEnsemblEntries should be 102915" in {
      ensembl.size must equalTo(102915)
    }

    "accession of first protein should be ENSP00000452494.1" in {
       ensembl.head.accession must equalTo("ENSP00000452494.1")
    }

    "accession of first protein should be ENSG00000228985.1" in {
      ensembl.head.geneAccession must equalTo("ENSG00000228985.1")
    }

    "accession of first protein should be ENST00000448914.1" in {
      ensembl.head.transcriptAccession must equalTo("ENST00000448914.1")
    }

  }

}
