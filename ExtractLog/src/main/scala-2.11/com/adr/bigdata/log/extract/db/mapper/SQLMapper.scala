package com.adr.bigdata.log.extract.db.mapper

import java.sql.ResultSet

import com.adr.bigdata.log.extract.db.bean.AbstractBean

/**
  * Created by noind on 1/5/2016.
  */
trait SQLMapper[T <: AbstractBean] {
  def map(i: Int, rs: ResultSet) : T
}
