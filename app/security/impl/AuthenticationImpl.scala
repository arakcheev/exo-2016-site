/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security.impl

import com.google.inject.Inject
import play.api.{Configuration, Logger}
import play.api.mvc.RequestHeader
import security.Authentication
import security.models.{User, UserRepository}
import services.PasswordCrypto
import models.parseId

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class AuthenticationImpl @Inject()(passwordCrypto: PasswordCrypto,
                                   userRepository: UserRepository,
                                   configuration: Configuration)(implicit val executionContext: ExecutionContext) extends Authentication {
  private val logger = Logger(getClass)

  val SESSION_NAME = configuration.getString("sessionName").getOrElse(sys.error("Missing session name in config."))

  /**
    * Authenticate user by login (email) and password.
    *
    * @param email user login
    * @param password user uncrypted password
    * @return Some(user) if authentication was successful otherwise None
    */
  override def auth(email: String, password: String): Future[Option[User]] = {
    userRepository.byLogin(email).map {
      case Some(user) if passwordCrypto.check(password, user.password) => Some(user)
      case _ => None
    }
  }

  /**
    * Authenticate user by http request headers. Headers must contains cookie that will
    * authenticate user.
    *
    * @param requestHeader http request headers with cookies
    * @return Some(user) if authentication was successful otherwise None
    */
  override def auth(implicit requestHeader: RequestHeader): Future[Option[User]] = {
    requestHeader.session.get(SESSION_NAME).map { session =>
      parseId(session) match {
        case Success(id) => userRepository.byId(id)
        case Failure(ex) => Future(None)
      }
    }.getOrElse(Future(None))
  }
}
