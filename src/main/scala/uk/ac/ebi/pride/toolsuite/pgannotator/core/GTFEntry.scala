package uk.ac.ebi.pride.toolsuite.pgannotator.core


import uk.ac.ebi.pride.toolsuite.pgannotator.utils.{Constants, Utils}
import scala.language.implicitConversions

/**
  * A GTFEntry in the GTF file from ENSMBL (http://www.ensembl.org/info/website/upload/gff.html). The
  */
case class GTFEntry(chromosome:Chromosome.Chromosome, source: String, feature:String, start:Int, end:Int,
                    score:Double, strand: Strand.Strand, frame: Frame.Frame, var attributes: String){

  /*
   * A semicolon-separated list of tag-value pairs, providing additional information about each feature.
   */
  def getMapAttributes: Map[String, String] = attributes.split(Constants.GTF_ATTRIBUTE_SEP).map(_ split Constants.GTF_ATTRIBUTE_VALUE_SEP)
    .collect { case Array(k, v) => (k.trim, v) }(collection.breakOut)

  def transcriptIdentifier: String = getMapAttributes.getOrElse(Constants.GTF_TRANSCRIPT_IDENTIFIER_ATTRIBUTE, getMapAttributes.getOrElse(Constants.GTF_TRANSCRIPT_HAVANA_IDENTIFIER_ATTRIBUTE, null))

  def geneIdentifier: String = getMapAttributes.getOrElse(Constants.GTF_GENE_IDENTIFIER_ATTRIBUTE, getMapAttributes.getOrElse(Constants.GTF_GENE_HAVANA_IDENTIFIER_ATTRIBUTE, null))

  def isCDS: Boolean         = (feature == Constants.GTF_CDS_PROPERTY)

  def isGene: Boolean        = (feature == Constants.GTF_GENE_PROPERTY)

  def isTranscript: Boolean  = (feature == Constants.GTF_TRANSCRIPT_PROPERTY)

  def geneName: String       = getMapAttributes.getOrElse(Constants.GTF_GENE_NAME_ATTRIBUTE, null)

  def geneType:String        = getMapAttributes.getOrElse(Constants.GTF_GENE_TYPE_ATTRIBUTE, null)

  def geneStatus: String     = getMapAttributes.getOrElse(Constants.GTF_GENE_STATUS_ATTRIBUTE, null)

}

/**
  * Chromosome information containing the number and the chromosome name. Chromosome in GTF (http://www.ensembl.org/info/website/upload/gff.html).
  * Seqname (Chromosome) - name of the chromosome or scaffold; chromosome names can be given with or without the 'chr' prefix.
  * Important note: the seqname must be one used within Ensembl, i.e. a standard chromosome name or an Ensembl identifier such as a scaffold ID,
  * without any additional content such as species or assembly. See the example GFF output below.
  */
object Chromosome extends Enumeration{
  val CHR_1    = Chromosome("chr1",     1)
  val CHR_2    = Chromosome("chr2",     2)
  val CHR_3    = Chromosome("chr3",     3)
  val CHR_4    = Chromosome("chr4",     4)
  val CHR_5    = Chromosome("chr5",     5)
  val CHR_6    = Chromosome("chr6",     6)
  val CHR_7    = Chromosome("chr7",     7)
  val CHR_8    = Chromosome("chr8",     8)
  val CHR_9    = Chromosome("chr9",     9)
  val CHR_10   = Chromosome("chr10",   10)
  val CHR_11   = Chromosome("chr11",   11)
  val CHR_12   = Chromosome("chr12",   12)
  val CHR_13   = Chromosome("chr13",   13)
  val CHR_14   = Chromosome("chr14",   14)
  val CHR_15   = Chromosome("chr15",   15)
  val CHR_16   = Chromosome("chr16",   16)
  val CHR_17   = Chromosome("chr17",   17)
  val CHR_18   = Chromosome("chr18",   18)
  val CHR_19   = Chromosome("chr19",   19)
  val CHR_20   = Chromosome("chr20",   20)
  val CHR_21   = Chromosome("chr21",   21)
  val CHR_22   = Chromosome("chr22",   22)
  val CHR_X    = Chromosome("chrX",    23)
  val CHR_Y    = Chromosome("chrY",    24)
  val CHR_XY   = Chromosome("chrXY",   25)
  val CHR_M    = Chromosome("chrM",    26)
  val CHR_NA   = Chromosome("chrNA",   -1)
  val SCAFFOLD = Chromosome("Scaffold", 0)

  case class Chromosome(name: String, nName: Int) extends super.Val()

  def getChromosomeByName(name:String):Chromosome = {
    var index = 0
    var chr = if(name.toUpperCase().startsWith("CHR")) name.toUpperCase().replaceFirst("CHR", "") else name
    chr = if(name.toUpperCase().startsWith("CHROMOSOME")) name.toUpperCase().replaceFirst("CHROMOSOME", "") else name
    if(Utils.isNumeric(chr)) return getChromosomeByNumber(chr.toInt)
    if(chr.startsWith("GL") || chr.startsWith("KI") || chr.startsWith("JH") || chr.startsWith("KB")) return SCAFFOLD
    if(chr.equalsIgnoreCase("X")) return CHR_X
    if(chr.equalsIgnoreCase("Y")) return CHR_Y
    if(chr.equalsIgnoreCase("XY")) return CHR_XY
    if(chr.equalsIgnoreCase("M") || chr.equalsIgnoreCase("MT")) return CHR_M
    CHR_NA
  }

  def getChromosomeByNumber(nName: Integer): Chromosome = {
    if(nName == 1) return CHR_1
    if(nName == 2) return CHR_2
    if(nName == 3) return CHR_3
    if(nName == 4) return CHR_4
    if(nName == 5) return CHR_5
    if(nName == 6) return CHR_6
    if(nName == 7) return CHR_7
    if(nName == 8) return CHR_8
    if(nName == 9) return CHR_9
    if(nName == 10) return CHR_10
    if(nName == 11) return CHR_11
    if(nName == 12) return CHR_12
    if(nName == 13) return CHR_13
    if(nName == 14) return CHR_14
    if(nName == 15) return CHR_15
    if(nName == 16) return CHR_16
    if(nName == 17) return CHR_17
    if(nName == 18) return CHR_18
    if(nName == 19) return CHR_19
    if(nName == 20) return CHR_20
    if(nName == 21) return CHR_21
    if(nName == 22) return CHR_22
    if(nName == 23) return CHR_X
    if(nName == 24) return CHR_Y
    if(nName == 25) return CHR_XY
    if(nName == 26) return CHR_M
    if(nName == 0) return SCAFFOLD
    CHR_NA
  }
  implicit def convert(value: Value) = value.asInstanceOf[Chromosome]
}

/**
  * Strand contain the information about Gene Strand.
  * defined as + (forward) or - (reverse). If the information is: . is NULL
  */
object Strand extends Enumeration {
  val FORWARD  = Strand("fdw", 1)
  val REVERSE  = Strand("rev", -1)
  val UNKNOWN  = Strand("unknown", 0)
  case class Strand(name: String, nName: Int) extends super.Val()
  def getStrandByName(name: String): Strand = {
    if(name.equalsIgnoreCase("-1") || name.equalsIgnoreCase("-")) return REVERSE
    if(name.equalsIgnoreCase("1")  || name.equalsIgnoreCase("+")) return FORWARD
    if(name.equalsIgnoreCase("0")  || name.equalsIgnoreCase(".")) return UNKNOWN
    UNKNOWN
  }
  implicit def convert(value: Value) = value.asInstanceOf[Strand]
}

/**
  * The three different frame and an additional Unknown.
  * frame - One of '0', '1' or '2'. '0' indicates that the first base of the feature is the first base of a codon,
  * '1' that the second base is the first base of a codon, and so on..
  */
object Frame extends Enumeration {

  val FRAME_1  = Frame("frame1", 1)
  val FRAME_2  = Frame("frame2", 2)
  val FRAME_3  = Frame("frame3", 3)
  val UNKNOWN  = Frame("unknown", 0)
  case class Frame(name: String, nName: Int) extends super.Val()
  implicit def convert(value: Value) = value.asInstanceOf[Frame]

  def getFrameByName(name:String): Frame = {
    if(name.equalsIgnoreCase("1")) return FRAME_1
    if(name.equalsIgnoreCase("2")) return FRAME_2
    if(name.equalsIgnoreCase("3") || name.equalsIgnoreCase("0")) return FRAME_3
    UNKNOWN
  }
}


/**
  * The three different Offset
  */
object Offset extends Enumeration {
  val OFFSET_1  = Offset("off1", 1)
  val OFFSET_2  = Offset("off2", 2)
  val OFFSET_3  = Offset("off3", 3)
  val DEFAULT   = Offset("off0", 0)
  case class Offset(name: String, nName: Int) extends super.Val()
  implicit def convert(value: Value) = value.asInstanceOf[Offset]
}

