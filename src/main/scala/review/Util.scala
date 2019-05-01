package review

import cats.data.EitherT
import cats.effect.IO

import scala.util.Try
import scala.util.control.NonFatal

object Util {

  case class Exception(message: String)

  def eitherTWrapper[A](f: => A): EitherT[IO, Exception, A] = EitherT {
    IO {
      Try(f).toEither.left
        .map {
          case NonFatal(e) =>
            Exception(e.getMessage)
        }
    }
  }

  def eitherTWrapper[L <: Throwable, R](
      value: Either[L, R]
  ): EitherT[IO, Exception, R] = EitherT {
    IO {
      value.left
        .map {
          case e =>
            Exception(e.getMessage)
        }
    }
  }
}
