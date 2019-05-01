package design

import Scenario3._

trait UserProvider[A <: User] {
  def update(user: A, levelUp: Int, remainingExp: Int): A
}

object UserProvider {

  def apply[A <: User: UserProvider]: UserProvider[A] =
    implicitly[UserProvider[A]]

  def update[A <: User: UserProvider](
      user: A,
      levelUp: Int,
      remainingExp: Int
  ): A = UserProvider[A].update(user, levelUp, remainingExp)

  implicit object FreeUserProvider extends UserProvider[FreeUser] {

    def update(user: FreeUser, levelUp: Int, remainingExp: Int): FreeUser =
      user copy (postsLimit = 3,
      experience = remainingExp,
      level = levelUp)
  }

  implicit object PaidUserProvider extends UserProvider[PaidUser] {

    def update(user: PaidUser, levelUp: Int, remainingExp: Int): PaidUser =
      user copy (remainingDays =
        if (user.remainingDays == 0) 0 else user.remainingDays - 1,
      experience = remainingExp,
      level = levelUp)
  }
}
