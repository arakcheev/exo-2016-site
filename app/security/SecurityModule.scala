/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package security

import com.google.inject.Singleton
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}
import security.impl.{AuthenticationImpl, UserRepositoryImplProvider}
import security.models.UserRepository

@Singleton
private class SecurityModule extends Module {

  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {

    val userRepoBind = bind[UserRepository].toProvider[UserRepositoryImplProvider]

    val authBind = bind[Authentication].to[AuthenticationImpl]

    Seq(userRepoBind, authBind)
  }
}
