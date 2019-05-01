package review

import donotmodifyme.Scenario1._
import Util._

import cats.effect.IO
import cats.data.EitherT

object Scenario1 {

  def process(total: Int): IO[Either[Exception, Int]] = {
    def helper(total: Int = total, n: Int = 0): EitherT[IO, Exception, Int] = {
      if (total <= 0) {
        EitherT[IO, Exception, Int] {
          IO.pure(Right(n))
        }
      } else {
        eitherTWrapper(blackBoxPositiveInt).flatMap(
          value => helper(total - value, n + 1)
        )
      }
    }

    helper().value
  }
}
