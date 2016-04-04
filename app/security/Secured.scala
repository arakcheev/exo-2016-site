/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import play.api.mvc._
import security.models.User

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * Secured request that wraps ordinary request with secured user.
  *
  * @param user user which performed request
  * @param request http request
  */
class SecuredRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)

/**
  * Secured action
  */
class Secured @Inject()(authenticator: Authentication) extends ActionBuilder[SecuredRequest] with LazyLogging {

  private implicit val prep = executionContext.prepare()

  override def invokeBlock[A](request: Request[A], block: (SecuredRequest[A]) => Future[Result]): Future[Result] = {

    authenticator.auth(request).flatMap {
      case Some(user) => block(new SecuredRequest[A](user, request))
      case None => Future(Results.Unauthorized)
    } recover {
      case NonFatal(e) =>
        logger.error("Error in secured action.", e)
        Results.InternalServerError("Error occurred.")
    }
  }
}
