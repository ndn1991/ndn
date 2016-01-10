package com.adr.bigdata.mining

import java.io.FileWriter

import org.apache.spark.ml.feature.{NGram, RegexTokenizer}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by noind on 1/8/2016.
  * create one gram + two gram
  */
object Prepare {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Test word2vec")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val regexTokenizer = new RegexTokenizer().setInputCol("sentence").setOutputCol("words").setPattern("\\s+")
    val ngram = new NGram().setInputCol("words").setOutputCol("ngram").setN(2)

    val dataDF = sqlContext.read.text("D:/data.dat", "D:/hihi.txt").toDF("sentence")
    val _words = regexTokenizer.transform(dataDF)
    val _2words = ngram.transform(_words)
    val fileWriter = new FileWriter("D:/x.txt")
    _words.select("words").collect().map(x => x.getSeq(0).mkString("|")).foreach(x => fileWriter.append(s"$x\n"))
    _2words.select("ngram").collect().map(x => x.getSeq(0).mkString("|")).foreach(x => fileWriter.append(s"$x\n"))
    fileWriter.close()
  }
}
