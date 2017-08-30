/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security.impl

import com.google.inject.{Inject, Provider, Singleton}
import database.Driver
import models.{Collection, Id, newId}
import play.api.{Configuration, Logger}
import security.models.{User, UserRepository}
import services.PasswordCrypto

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl
(collection: Collection[User],
    passwordCrypto: PasswordCrypto,
    configuration: Configuration)
  (implicit val executionContext: ExecutionContext) extends UserRepository {

  private val logger = Logger(getClass)

  def init ={
    val login = configuration.getString("secure.login").getOrElse(sys.error("secure.login"))
    val password = configuration.getString("secure.password").getOrElse(sys.error("secure.password"))
    val user = User(newId, login, passwordCrypto.hash(password))
    byLogin(user.login).flatMap{
      case None ⇒ collection.insertOne(user).toFuture.map(_ ⇒ logger.warn("Admin user initialize"))
      case Some(_) ⇒ Future(())
    }
  }

  init

  /**
    * Get user by login
    *
    * @param login user login (ordinary, email)
    * @return Some[User] if user found, otherwise None
    */
  override def byLogin(login: String): Future[Option[User]] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("login",login)
    collection.find(query).head().map(Option.apply)
  }

  /**
    * Get user by id
    *
    * @param id user id
    * @return Some[User] if user found, otherwise None
    */
  override def byId(id: Id): Future[Option[User]] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("_id", id)
    collection.find(query).head().map(Option.apply)
  }
}

@Singleton
class UserRepositoryImplProvider @Inject()(configuration: Configuration, driver: Driver, executionContext: ExecutionContext, passwordCrypto: PasswordCrypto) extends Provider[UserRepository] {

  val collection: Collection[User] = driver.database.withCodecRegistry(User.codecRegistry).getCollection[User]("users")


  override def get(): UserRepository = new UserRepositoryImpl(collection, passwordCrypto, configuration)(executionContext)
}
