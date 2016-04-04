/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security

import play.api.mvc.RequestHeader
import security.models.User

import scala.concurrent.Future

/**
  * This trait provide user authentication mechanics into the site.
  */
trait Authentication {

  val SESSION_NAME: String

  /**
    * Authenticate user by login (email) and password.
    *
    * @param email user login
    * @param password user uncrypted password
    * @return Some(user) if authentication was successful otherwise None
    */
  def auth(email: String, password: String): Future[Option[User]]

  /**
    * Authenticate user by http request headers. Headers must contains cookie that will
    * authenticate user.
    *
    * @param requestHeader http request headers with cookies
    * @return Some(user) if authentication was successful otherwise None
    */
  def auth(implicit requestHeader: RequestHeader): Future[Option[User]]
}
