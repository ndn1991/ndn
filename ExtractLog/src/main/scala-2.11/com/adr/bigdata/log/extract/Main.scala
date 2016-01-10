package com.adr.bigdata.log.extract

import com.typesafe.config.ConfigFactory

/**
  * Created by noind on 1/5/2016.
  */
object Main {
  val config = ConfigFactory.load(System.getProperty("conf.path", "application.conf"))
  val username = config.getString("db.username")
  val password = config.getString("db.password")
  val host = config.getString("db.host")
  val port = config.getInt("db.port")
  val dbName = config.getString("db.dbName")

  def main(args: Array[String]) {
    ExtractSearchLog.extract(args(0), username, password, host, port, dbName)
  }

  //  def go(): Unit ={
  //    val jsch = new JSch()
  //    val session = jsch.getSession("root", "10.220.75.78", 22)
  //    session.setPassword("rootroot")
  //    session.setConfig("StrictHostKeyChecking", "no")
  //    println("Establishing Connection...")
  //    session.connect()
  //    val assignedPort = session.setPortForwardingL(lport, rhost, rport)
  //    println(s"localhost:$assignedPort -> $rhost:$rport")
  //  }
}
