package com.adr.bigdata.log.extract

import org.slf4j.LoggerFactory

/**
  * Created by noind on 1/5/2016.
  */
trait Loggable {
  def getLogger() = LoggerFactory.getLogger(getClass)
}
