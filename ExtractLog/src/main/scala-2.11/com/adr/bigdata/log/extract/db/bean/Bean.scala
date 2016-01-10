package com.adr.bigdata.log.extract.db.bean

import java.sql.Blob

import com.adr.bigdata.log.extract.Loggable

/**
  * Created by noind on 1/5/2016.
  */
abstract class AbstractBean extends Loggable
case class UserLogBean(sessionId: Blob, curUrl: String, refUrl: String, recordedTime: Long) extends AbstractBean
