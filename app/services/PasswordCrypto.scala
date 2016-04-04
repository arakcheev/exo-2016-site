/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package services

import com.github.t3hnar.bcrypt.Password
import com.github.t3hnar.bcrypt.generateSalt
import com.google.inject.ImplementedBy
import scala.language.implicitConversions

@ImplementedBy(classOf[PasswordCryptoImlp])
trait PasswordCrypto {
  def hash(password: String): String

  def check(password: String, hash: String): Boolean

  def random: String
}

private class PasswordCryptoImlp extends PasswordCrypto {

  def hash(password: String) = new Password(password).bcrypt

  def check(password: String, hash: String) = new Password(password).isBcrypted(hash)

  def random = generateSalt

}
