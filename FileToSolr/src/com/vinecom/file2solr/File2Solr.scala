package com.vinecom.file2solr

import scala.io.Source

/**
 * @author noind
 */
object File2Solr {
  def main(args: Array[String]): Unit = {
   	Source.fromFile(args(0))
   	.getLines()
   	.map(line => {
   		val parts = line.substring(1, line.length() - 2).split(",", 2);
   		val oriItem = parts(0).toInt
   		val neighs = parts(1).split("#").map(s => s.substring(1, s.length() - 2).split(",", 2) match {case Array(item, sim) => (item.toInt, sim.toDouble)})
   		(oriItem, neighs)
   	})
   	
  }
}