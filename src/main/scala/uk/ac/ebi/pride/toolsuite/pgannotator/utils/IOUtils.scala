package uk.ac.ebi.pride.toolsuite.pgannotator.utils

import java.io.{File, FileInputStream, InputStream, RandomAccessFile}
import java.util.zip.GZIPInputStream

import scala.io.BufferedSource

/**
  *
  *  @author ypriverol
  */
object IOUtils {

  def inputSource(fname: File, gzip: Boolean): BufferedSource = {
    io.Source.fromInputStream(
      if (gzip) new GZIPInputStream(new FileInputStream(fname))
      else new FileInputStream(fname))("UTF-8")
  }

  def inputStream(fname:File, gzip:Boolean): InputStream = if(gzip) new GZIPInputStream(new FileInputStream(fname)) else new FileInputStream(fname)

  def isGZip(file:File) : Boolean = {
    val raf = new RandomAccessFile(file, "r")
    GZIPInputStream.GZIP_MAGIC == (raf.read() & 0xff | ((raf.read() << 8) & 0xff00))
  }

}
