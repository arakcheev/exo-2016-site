/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security.impl

import com.google.inject.{Inject, Provider}
import com.typesafe.scalalogging.LazyLogging
import database.Driver
import models.{Collection, Id, document}
import play.api.Configuration
import security.models.{User, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl(collection: Collection)(implicit val executionContext: ExecutionContext) extends UserRepository with LazyLogging {
  /**
    * Get user by login
    *
    * @param login user login (ordinary, email)
    * @return Some[User] if user found, otherwise None
    */
  override def byLogin(login: String): Future[Option[User]] = {
    val query = document("login" -> login)
    collection.find(query).one[User]
  }

  /**
    * Get user by id
    *
    * @param id user id
    * @return Some[User] if user found, otherwise None
    */
  override def byId(id: Id): Future[Option[User]] = {
    val query = document("_id" -> id)
    collection.find(query).one[User]
  }
}

class UserRepositoryImplProvider @Inject()(configuration: Configuration, driver: Driver, executionContext: ExecutionContext) extends Provider[UserRepository] {

  val collection = driver.collection("users")

  override def get(): UserRepository = new UserRepositoryImpl(collection)(executionContext)
}
