package com.adr.bigdata.log.extract.db.daoi

import java.sql.Statement

import com.adr.bigdata.log.extract.db.bean.UserLogBean
import com.adr.bigdata.log.extract.db.mapper.UserLogMapper

/**
  * Created by noind on 1/5/2016.
  */
class UserLogDAO(stmt: Statement) extends AbstractDAOI(stmt = stmt) {
  def userLog(): Iterator[UserLogBean] = {
    beans("select user_id, visitor_id, current_url, referrer_url, action from action_log where environment_id=3", UserLogMapper)
  }
}
