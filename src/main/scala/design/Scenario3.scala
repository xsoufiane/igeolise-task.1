package design

object Scenario3 {

  trait User {
    def name: String
    def username: String
    def experience: Int
    def level: Int

    require(
      username.matches("^[a-zA-Z0-9-._]*$") && level >= 0 && experience >= 0
    )

    def increaseExperience: User
    def post: Either[LimitReachedException, User]
  }

  object User {
    val PostsLimit = 3
  }

  case class LimitReachedException(message: String)

  case class FreeUser(
      name: String,
      username: String,
      experience: Int = 0,
      level: Int = 0,
      postsLimit: Int = User.PostsLimit
  ) extends User {

    def increaseExperience: FreeUser = copy(experience = experience + 1)

    def post: Either[LimitReachedException, FreeUser] = {
      if (postsLimit == 0) {
        Left(
          LimitReachedException(
            "You have reached your post limit. Please wait another day or update your account."
          )
        )
      } else {
        Right(copy(experience = experience + 1, postsLimit = postsLimit - 1))
      }
    }
  }

  case class PaidUser(
      remainingDays: Int,
      name: String,
      username: String,
      experience: Int = 0,
      level: Int = 0
  ) extends User {

    def increaseExperience: PaidUser = copy(experience = experience + 1)

    def post: Either[LimitReachedException, PaidUser] = {
      if (remainingDays == 0) {
        Left(
          LimitReachedException(
            "You are not allowed to perform this operation anymore. Please check your account limit."
          )
        )
      } else {
        Right(copy(experience = experience + 1))
      }
    }
  }
}
