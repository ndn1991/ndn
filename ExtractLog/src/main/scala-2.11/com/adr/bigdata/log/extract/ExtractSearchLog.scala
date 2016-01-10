package com.adr.bigdata.log.extract

import java.io.FileWriter
import java.net.URLDecoder
import java.sql.{DriverManager, ResultSet}
import javax.script.ScriptEngineManager

import com.adr.bigdata.log.extract.db.mapper.UserLogMapper
import org.hashids.Hashids

/**
  * Created by noind on 1/5/2016.
  */
object ExtractSearchLog extends Loggable {
  val key = "87555533333222220000008888866Ss5"
  val hashIds = new Hashids(key)
  val factory = new ScriptEngineManager()
  val engine = factory.getEngineByName("JavaScript")
  val searchR = ".*www.adayroi.com/tim-kiem(.*)".r
  val detailR = ".*www.adayroi.com/(.*?)-p-(.*?)-f(.*?)-(.*?)?pi=(.*?)&w=.*".r
  val prefixR = ".*www.adayroi.com/(.*)".r

  def extract(path: String, username: String, password: String, host: String, port: Int, dbName: String): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    val url = s"jdbc:mysql://$host:$port/$dbName"
    val conn = DriverManager.getConnection(url, username, password)
    val stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    stmt.setFetchSize(Int.MinValue)
    val searchFW = new FileWriter(s"$path/search.txt", true)
    val detailFW = new FileWriter(s"$path/detail.txt", true)

    val rs = stmt.executeQuery("select visitor_id, current_url, referrer_url, recorded_time from action_log where environment_id=3 and referrer_url like '%tim-kiem%'")
    try {
      val start = System.currentTimeMillis()
      while (rs.next) {
        val bean = UserLogMapper.map(0, rs)
        if (bean.refUrl.contains("/tim-kiem")) {
          val keyword = removePrefix(bean.refUrl)
          if (keyword != null) {
            val params = allParams(bean.curUrl)
            if (params != null) {
              // divide use case here
              params match {
                case search: Search =>
                  val blob = new String(bean.sessionId.getBytes(1L, bean.sessionId.length().asInstanceOf[Int]))
                  searchFW.write(s"$blob :::: ${bean.recordedTime} :::: ${decode(keyword)} :::: ${decode(search.keyword)}\n")
                case d: Detail =>
                  val blob = new String(bean.sessionId.getBytes(1L, bean.sessionId.length().asInstanceOf[Int]))
                  detailFW.write(s"$blob :::: ${bean.recordedTime} :::: ${decode(keyword)} :::: ${d.productId}\n")
              }
            }
          }
        }
      }
      println(s"total time ${System.currentTimeMillis() - start}")
    } finally {
      rs.close()
      stmt.close()
      conn.close()

      searchFW.close()
      detailFW.close()
    }
  }

  def decode(link: String): String = {
    try {
      URLDecoder.decode(link, "UTF-8")
    } catch {
      case e: Exception => engine.eval(s"unescape('$link')").asInstanceOf[String]
    }
  }

  private def allParams(curLink: String) = {
    try {
      curLink match {
        case searchR(other) => Search(other)
        case detailR(x, productId, productType, freshFoodType, productItemId) => Detail(decodeProductId(productId))
        case _ => null
      }
    } catch {
      case e: Exception => null
    }
  }

  def decodeProductId(code: String) = hashIds.decode(code).head.toInt

  def main(args: Array[String]) {
    println(removePrefix("https://www.adayroi.com/the-sky-does-not-have-to-be-blue-p-kll22-f1-2?pi=nYk5J&w=GgV"))
  }

  private def removePrefix(link: String) = {
    link match {
      case prefixR(tail) => tail
      case _ => null
    }
  }
}

case class Search(keyword: String)

case class Detail(productId: Int)
