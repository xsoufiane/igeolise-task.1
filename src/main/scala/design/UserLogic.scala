package design

import Scenario3.User

object UserLogic {

  def runAtMidnight[A <: User: UserProvider](user: A): A = {
    val nLevel: Int       = user.experience / 1000
    val levelUp: Int      = user.level + nLevel
    val remainingExp: Int = user.experience % 1000

    UserProvider.update[A](user, levelUp, remainingExp)
  }
}
