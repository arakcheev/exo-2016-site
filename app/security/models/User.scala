/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security.models

import models.Id
import reactivemongo.bson.Macros

import scala.concurrent.Future

case class User(_id: Id, login: String, password: String)

object User {
  implicit val handler = Macros.handler[User]
}

/**
  * User repository access methods.
  */
trait UserRepository {

  /**
    * Get user by login
    *
    * @param login user login (ordinary, email)
    * @return Some[User] if user found, otherwise None
    */
  def byLogin(login: String): Future[Option[User]]

  /**
    * Get user by id
    *
    * @param id user id
    * @return Some[User] if user found, otherwise None
    */
  def byId(id: Id): Future[Option[User]]
}
