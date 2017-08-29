/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security.impl

import com.google.inject.{Inject, Provider, Singleton}
import database.Driver
import models.{Collection, Id, document, newId}
import play.api.{Configuration, Logger}
import security.models.{User, UserRepository}
import services.PasswordCrypto

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl
(collection: Collection,
    passwordCrypto: PasswordCrypto,
    configuration: Configuration)
  (implicit val executionContext: ExecutionContext) extends UserRepository {

  private val logger = Logger(getClass)

  def init ={
    val login = configuration.getString("secure.login").getOrElse(sys.error("secure.login"))
    val password = configuration.getString("secure.password").getOrElse(sys.error("secure.password"))
    val user = User(newId, login, passwordCrypto.hash(password))
    byLogin(user.login).flatMap{
      case None ⇒ collection.insert(user).map(_ ⇒ logger.info("Admin user initialize"))
      case Some(_) ⇒ Future(())
    }
  }

  init


  //  val user = User(newId, "admin@inasan.ru", passwordCrypto.hash("123"))
//  collection.insert(user).map(_ => println("saved"))

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

@Singleton
class UserRepositoryImplProvider @Inject()(configuration: Configuration, driver: Driver, executionContext: ExecutionContext, passwordCrypto: PasswordCrypto) extends Provider[UserRepository] {

  val collection = driver.collection("users")

  override def get(): UserRepository = new UserRepositoryImpl(collection, passwordCrypto, configuration)(executionContext)
}
