/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package database

import com.google.inject.{Inject, Singleton}
import com.mongodb.ConnectionString
import org.mongodb.scala.MongoClient
import play.api.Configuration

@Singleton
class Driver @Inject()(configuration: Configuration){
  private val mongoUrl = configuration.getString("mongodb.uri").get
  private val conntectionString = new ConnectionString(mongoUrl)
  private val dbName = conntectionString.getDatabase
  val mongoClient: MongoClient = MongoClient(mongoUrl)
  val database = mongoClient.getDatabase(dbName)
}