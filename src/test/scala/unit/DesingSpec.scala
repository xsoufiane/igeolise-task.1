package unit

import org.scalatest.FunSuite
import design._
import Scenario3._

class DesingSuite extends FunSuite {

  trait Users {
    val freeUser = FreeUser("", "")
    val freeUserWithPostsLimitReached =  FreeUser("", "", postsLimit = 0)
    val paidUser = PaidUser(2, "", "")
    val paidUserWithZeroCredit = PaidUser(0, "", "")
    val midnightFreeUser = FreeUser("", "user", experience = 3500)
    val midnightPaidUser = PaidUser(1, "", "user", experience = 3500)

  }

  test("Username should only include [a-z][A-Z][0-9]-._") {
    withClue("Issue with free user") {
      assertThrows[IllegalArgumentException](FreeUser("", "user*"))
    }

    withClue("Issue with paid user.") {
      assertThrows[IllegalArgumentException](PaidUser(2, "", "user*"))
    }
  }

  test("Experience should always be positive") {
    withClue("Issue with free user") {
      assertThrows[IllegalArgumentException](FreeUser("", "user", experience = -1))
    }

    withClue("Issue with paid user.") {
      assertThrows[IllegalArgumentException](PaidUser(2, "", "user", experience = -1))
    }
  }

  test("Level should always be positive.") {
    withClue("Issue with free user") {
      assertThrows[IllegalArgumentException](FreeUser("", "user" ,level = -1))
    }

    withClue("Issue with paid user.") {
      assertThrows[IllegalArgumentException](PaidUser(2, "", "user", level = -1))
    }
  }

  test("Increase user experience") {
    new Users {
      assert(freeUser.increaseExperience.experience === 1 && paidUser.increaseExperience.experience === 1)
    }
  }

  test("Free user posting increases experience and reduces rate") {
    new Users {
      val freeUserPosting = freeUser.post
      assert(freeUserPosting.isRight)
      assert(freeUserPosting.right.get.experience === 1 && freeUserPosting.right.get.postsLimit === 2)
    }
  }

  test("Free user can no longer post once its limit is reached") {
    new Users {
      assert(freeUserWithPostsLimitReached.post.isLeft)
    }
  }

  test("Paid user posting increases experience") {
    new Users {
      val paidUserPosting = paidUser.post
      assert(paidUserPosting.isRight)
      assertResult(1)(paidUserPosting.right.get.experience)
    }
  }

  test("Paid user can no longer post after days credit achieved") {
    new Users {
      assert(paidUserWithZeroCredit.post.isLeft)
    }
  }

  test("Midnight logic") {
    new Users {
      val leveledUpFreeUser = UserLogic.runAtMidnight(midnightFreeUser)
      val leveledUpPaidUser = UserLogic.runAtMidnight(midnightPaidUser)

      withClue("Issue with free user logic.") {
        assert(leveledUpFreeUser.experience === 500 && leveledUpFreeUser.level === 3)
      }

      withClue("Issue with paid user logic.") {
        assert(leveledUpPaidUser.experience === 500 && leveledUpPaidUser.level === 3)
      }
    }
  }
}
