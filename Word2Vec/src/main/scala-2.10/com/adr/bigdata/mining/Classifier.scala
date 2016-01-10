package com.adr.bigdata.mining

import java.io.{BufferedReader, FileReader}

import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Created by noind on 1/8/2016.
  */
object Classifier {
  val keyword2CatFile = "D:/hehe.txt"

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SVM Test").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val model = loadModel()
    val _data = loadData(keyword2CatFile, sc, sqlContext, model)
    val catMap = _data._1
    val size = catMap.size
    val data = _data._2.map{case(x, y) => (x.toDouble, y)}.filter(_._2 != null).toDF("label", "features").randomSplit(Array(0.8, 0.2), seed = 1234L)
    val train = data(0)
    val test = data(1)

    val layers = Array[Int](100, 150, 500, size)
    val trainer = new MultilayerPerceptronClassifier()
      .setLayers(layers)
      .setBlockSize(128)
      .setSeed(1234L)
      .setMaxIter(100)

    val _model = trainer.fit(train)
    val result = _model.transform(test)
    val predictionAndLabels = result.select("prediction", "label")
    val evaluator = new MulticlassClassificationEvaluator()
      .setMetricName("precision")
    println("Precision:" + evaluator.evaluate(predictionAndLabels))
  }

  def loadData(path: String = keyword2CatFile, sc: SparkContext, sqlContext: SQLContext, model: Map[String, Array[Double]]): (Map[Int, Int], RDD[(Int, DenseVector)]) = {
    val data = sc.textFile(path, 16).map(_.split(" :::: ") match { case Array(keyword, catId, catName) => (keyword, catId.toInt) })
    val catIds = data.map(_._2).collect().distinct.zipWithIndex.toMap
    val catIdsBV = sc.broadcast(catIds)
    val modelBV = sc.broadcast(model)
    val vectors = data.mapPartitions(it => {
      val _catIds = catIdsBV.value
      val _model = modelBV.value
      it.map { case (keyword, catId) => (keyword, _catIds(catId)) }
        .map { case (keyword, catId) => (catId, vectorOf(keyword, _model)) }
    })
    val catMap = catIds.map { case (value, index) => (index, value) }
    (catMap, vectors)
  }

  def vectorOf(keyword: String, model: Map[String, Array[Double]]): DenseVector = {
    val es = keyword.split("\\s+")
    val vector = new Array[Double](100)
    var count = 0
    for (e <- es) {
      val v = model.getOrElse(e, null)
      if (v != null) {
        for (i <- 0 until 100) {
          vector(i) = vector(i) + v(i)
        }
        count += 1
      }
    }
    if (count == 0) {
      null
    } else {
      for (i <- 0 until 100) {
        vector(i) = vector(i) / count
      }
      new DenseVector(vector)
    }
  }

  def loadModel(path: String = "D:/model"): Map[String, Array[Double]] = {
    val map = new mutable.HashMap[String, Array[Double]]()
    val br = new BufferedReader(new FileReader(path))
    Stream.continually(br.readLine()).takeWhile(_ != null).foreach(line => {
      val es = line.split(" :::: ")
      map.put(es(0), toArray(es(1)))
    })
    println("Loaded model")
    map.toMap
  }

  def toArray(s: String): Array[Double] = {
    val sArr = s.substring(1, s.length - 1)
    sArr.split(",").map(_.toDouble)
  }
}
