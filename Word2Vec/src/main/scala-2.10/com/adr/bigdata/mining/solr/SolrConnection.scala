package com.adr.bigdata.mining.solr

import org.apache.http.HttpHost
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.conn.params.ConnRoutePNames
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.solr.client.solrj.impl.HttpSolrClient

/**
  * Created by noind on 1/8/2016.
  * Store and manage solr connection
  */
object SolrConnection {
  private val httpClient = new DefaultHttpClient()
  private val proxy = new HttpHost("10.220.67.34", 80, "http")
  httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy)
  httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("bdteam", "Vinecom2015"))

  val solrClient = new HttpSolrClient("http://10.220.67.34/solr/product", httpClient)

  Runtime.getRuntime.addShutdownHook(new Thread() {
    {
      this.setName("Shutdown thread")
      this.setPriority(Thread.MAX_PRIORITY)
    }

    override def run(): Unit = {
      httpClient.close()
      solrClient.close()
    }
  })
}
