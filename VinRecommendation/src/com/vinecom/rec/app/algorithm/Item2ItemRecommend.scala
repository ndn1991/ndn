package com.vinecom.rec.app.algorithm

import scala.collection.mutable
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import com.vinecom.common.scala.tool.topK.ArrayTopK
import com.vinecom.common.scala.tool.topK.TopK
import com.vinecom.rec.app.ultil.Utils.getLong
import com.vinecom.rec.app.ultil.Utils.keyOnFirstItem
import org.apache.spark.sql.SQLContext
import com.vinecom.rec.app.vo.config.ItemBaseConfig
import org.apache.spark.HashPartitioner
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
/**
 * @author ndn
 */
object Item2ItemRecommend {
	var config: ItemBaseConfig = null
	var sc: SparkContext = null
	var sqlContext: SQLContext = null
	val partitioner = new HashPartitioner(128)

	def getRec(data: RDD[(Long, Int, Double)]) = {
		val maxItemId = data.map{case (u, i, r) => i}.max() + 1
		val rates = data.map { case (u, i, r) => (u, (i, r)) }
			.groupByKey()
			.filter{case (u, r) => r.size > 2}			
			.values
			.map(arr => Vectors.sparse(maxItemId, arr.toSeq))
		val rowMatrix = new RowMatrix(rates)
		val pairItemSims = rowMatrix.columnSimilarities(config.recConfig.threshold)
			.entries
			.map(entry => ((entry.i, entry.j), entry.value))
		pairItemSims.flatMap(keyOnFirstItem)
			.combineByKey(v => new ArrayTopK[Long](config.recConfig.numNeigh).put(v),
				(topK: TopK[Long], v) => topK.put(v),
				(topK1: TopK[Long], topK2: TopK[Long]) => topK1.put(topK2))
	}

	def init(_config: ItemBaseConfig, _sc: SparkContext) {
		config = _config
		sc = _sc
		sqlContext = new SQLContext(sc)
	}
	
	def getVirtualRate() = {
		val userProductOrder = getOrderData()
		val userProductView = getViewData()
		userProductOrder.fullOuterJoin(userProductView)
			.mapValues {
				case (Some(r1), Some(r2)) => (if (r1 > r2) r1 else r2)
				case (Some(r1), None) => r1
				case (None, Some(r2)) => r2
				case _ => -1.0
			}
			.filter { case ((u, i), r) => r != -1.0 }
			.map { case ((u, i), r) => (u, i, r) }
	}

	def getViewData() = {
		val jdbcDFView = sqlContext.read.format("jdbc").options(Map(
			"url" -> config.dbConnections("view").getUrl(),
			"driver" -> config.dbConnections("view").clazz,
			"dbtable" -> s"(${config.recConfig.queryView}) as tmp_xyz"))
		val dataView = jdbcDFView.load()
			.map(row => row.toSeq match {
				case Seq(userId: Int, visistorId: String, productId: Int) => (userId, visistorId.toLong, productId)
				case _ => null
			})
			.filter(_ != null)
		val visitorId2UserId = dataView.filter { case (u, v, p) => u > 0 }
			.map { case (u, v, p) => (v, u) }
			.collectAsMap
			.toMap
		val visitorId2UserIdBC = sc.broadcast(visitorId2UserId)
		val userProductView = dataView.mapPartitions(it => {
			val v2u = visitorId2UserIdBC.value
			it.map {
				case (u, v, p) => {
					if (u > 0) (u.toLong, p)
					else {
						if (v2u.contains(v)) (v2u(v).toLong, p)
						else (v, p)
					}
				}
			}
		}).map { case (u, i) => ((u, i), 1) }
			.combineByKey((once: Int) => once, (sum: Int, once: Int) => sum + once, (sum1: Int, sum2: Int) => sum1 + sum2, partitioner)
		val dataViewUserL2 = userProductView.map { case ((user, item), numView) => (user, numView) }
			.combineByKey(n => n * n, (sqr: Int, n) => sqr + n * n, (sqr1: Int, sqr2: Int) => sqr1 + sqr2)
			.mapValues(sqr => math.sqrt(sqr.toDouble))
			.collectAsMap()
			.toMap
		val dataViewUserL2Bc = sc.broadcast(dataViewUserL2)
		userProductView.mapPartitions(it => {
			val l2 = dataViewUserL2Bc.value
			it.map { case ((user, item), numView) => ((user, item), 5 * numView / l2(user)) }
		})
	}

	def getOrderData() = {
		val jdbcDFOms = sqlContext.read.format("jdbc").options(Map(
			"url" -> config.dbConnections("order").getUrl(),
			"driver" -> config.dbConnections("order").clazz,
			"dbtable" -> s"(${config.recConfig.queryOms}) as tmp_xyz"))
		jdbcDFOms.load()
			.map(row => row.toSeq match {
				case Seq(userId: String, orderId: Int, productId: Int, numProduct: Int) => (getLong(userId), productId, orderId)
				case _ => null
			})
			.filter(_ != null)
			.map { case (user, item, order) => ((user, item), 1) }
			.combineByKey((once: Int) => once, (sum: Int, once: Int) => sum + once, (sum1: Int, sum2: Int) => sum1 + sum2, partitioner)
			.mapValues(numOrder => if (numOrder > 1) 5.0 else 4.0)
	}
}