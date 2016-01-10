package com.adr.bigdata.mining.log

import java.io.{BufferedReader, FileReader, FileWriter}

import scala.collection.mutable

/**
  * Created by noind on 1/8/2016.
  * Extract log to keyword2Cat file
  */
object LogProcessor {
  val REMOVE_SPECIAL_CHARACTER = "\"|:|'|-|=|!|\\{|\\}|\\+|\\\\|/|\\^|\\(|\\)|\\[|\\]|#"

  val p2catFile = "D:/ps.dat"
  val catInfo = "D:/cat.txt"
  val detailLogFile = "D:/test/detail.txt"
  val resultFile = "D:/hehe.txt"

  val p2Cat = new mutable.HashMap[Int, Int]()
  val brP2Cat = new BufferedReader(new FileReader(p2catFile))
  Stream.continually(brP2Cat.readLine()).takeWhile(_ != null).foreach(line => {
    val es = line.split(" :::: ")
    p2Cat.put(es(0).toInt, es(1).toInt)
  })
  val cats = new mutable.HashMap[Int, String]()
  val brCats = new BufferedReader(new FileReader(catInfo))
  Stream.continually(brCats.readLine()).takeWhile(_ != null).foreach(line => {
    val es = line.split(" :::: ")
    cats.put(es(0).toInt, es(1))
  })

  def main(args: Array[String]) {
    val br = new BufferedReader(new FileReader(detailLogFile))
    val fw = new FileWriter(resultFile)
    Stream.continually(br.readLine()).takeWhile(_ != null).foreach(line => {
      val es = line.split(" :::: ")
      val eKeyword = es(2)
      val keyword = keywordFromURL(eKeyword)
      if (keyword != null) {
        val productId = es(3).toInt
        val catId = catIdFromProduct(productId)
        if (catId != null)
          fw.append(s"${keyword.replaceAll(REMOVE_SPECIAL_CHARACTER, " ").replaceAll("\\s+", " ").trim().toLowerCase} :::: ${catId._1} :::: ${catId._2}\n")
      }
    })
    fw.close()
  }

  def catIdFromProduct(productId: Int): (Int, String) = {
    val catId = p2Cat.getOrElse(productId, 0)
    if (catId == 0) {
      null
    } else {
      val catName = cats.getOrElse(catId, "")
      (catId, catName)
    }
  }

  def keywordFromURL(url: String): String = {
    try {
      val sParams = url.substring(url.indexOf('?') + 1)
      val params = sParams.split("&")
      var qParam: String = null
      for (param <- params) {
        if (param.startsWith("q=")) {
          qParam = param.substring(param.indexOf('=') + 1)
        }
      }
      qParam
    } catch {
      case e: Exception => null
    }
  }
}
