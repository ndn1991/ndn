package com.adr.bigdata.mining.log

import java.io.{BufferedReader, FileReader, FileWriter, StringReader}

import com.adr.bigdata.utils.ACSIIFolding
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter
import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

/**
  * Created by noind on 1/8/2016.
  * extract keyword2cat file to keyword log and its ascii folding
  */
object LogExtractor {
  val keyword2CatFile = "D:/hehe.txt"
  val outputBeforeNGram = "D:/hihi.txt"
  val acs = new ACSIIFolding()

  def main(args: Array[String]) {
    val brKeyword2CatFile = new BufferedReader(new FileReader(keyword2CatFile))
    val fw = new FileWriter(outputBeforeNGram)
    Stream.continually(brKeyword2CatFile.readLine()).takeWhile(_ != null).foreach(line => {
      val keyword = line.split(" :::: ")(0)
      val st = new StandardTokenizer()
      st.setReader(new StringReader(acs.transformToUTF8(keyword)))
      val filter = new ASCIIFoldingFilter(st, false)
      val term = filter.addAttribute(classOf[CharTermAttribute])
      filter.reset()
      val strbuf = new StringBuffer()
      while (filter.incrementToken()) {
        strbuf.append(term.toString).append(' ')
      }
      val acsWord = strbuf.deleteCharAt(strbuf.length() - 1).toString
      if (acsWord.equals(keyword)) {
        fw.append(s"$keyword\n")
      } else {
        fw.append(s"$keyword\n")
        fw.append(s"$acsWord\n")
      }
    })
    fw.close()
  }
}
