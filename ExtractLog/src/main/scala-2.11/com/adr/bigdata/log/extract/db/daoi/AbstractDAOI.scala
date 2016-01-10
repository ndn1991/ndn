package com.adr.bigdata.log.extract.db.daoi

import java.sql.Statement

import com.adr.bigdata.log.extract.db.bean.AbstractBean
import com.adr.bigdata.log.extract.db.mapper.SQLMapper

/**
  * Created by noind on 1/5/2016.
  */
abstract class AbstractDAOI ( stmt: Statement) {
  protected def beans[T <: AbstractBean](sql: String, mapper: SQLMapper[T]) = {
    val rs = stmt.executeQuery(sql)
    try {
      new Iterator[T] {
        override def hasNext: Boolean = rs.next()

        override def next(): T = mapper.map(0, rs)
      }
    } finally {
      rs.close()
    }
  }
}
