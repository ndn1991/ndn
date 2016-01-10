package com.adr.bigdata.mining

import java.io.FileWriter

import org.apache.spark.ml.feature.{RegexTokenizer, Word2Vec}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by noind on 1/7/2016.
  * Test word2vec algorithm
  */
object W2V {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Test word2vec")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val regexTokenizer = new RegexTokenizer().setInputCol("sentence").setOutputCol("words").setPattern("\\|")
    val word2Vec = new Word2Vec().setInputCol("words").setOutputCol("vectors").setVectorSize(100)

    val dataDF = sqlContext.read.text("D:/y.txt").toDF("sentence")
    val words = regexTokenizer.transform(dataDF)
    val model = word2Vec.fit(words)
//    model.findSynonyms("áo", 10).foreach(println)
    save(model.getVectors, "D:/model")
  }

  def save(data: DataFrame, path: String): Unit = {
    val fileWriter = new FileWriter(path)
    data.collect().foreach(x => fileWriter.append(s"${x.mkString(" :::: ")}\n"))
    fileWriter.close()
  }


}
