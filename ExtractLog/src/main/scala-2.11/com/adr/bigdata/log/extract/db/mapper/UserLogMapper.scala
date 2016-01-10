package com.adr.bigdata.log.extract.db.mapper

import java.sql.ResultSet

import com.adr.bigdata.log.extract.db.bean.UserLogBean

/**
  * Created by noind on 1/5/2016.
  */
object UserLogMapper extends SQLMapper[UserLogBean] {
  override def map(i: Int, rs: ResultSet): UserLogBean =
    UserLogBean(rs.getBlob("visitor_id"), rs.getString("current_url"), rs.getString("referrer_url"), rs.getTimestamp("recorded_time").getTime)
}

