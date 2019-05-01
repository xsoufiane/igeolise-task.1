package review

import donotmodifyme.Scenario2._
import Database._
import review.Util._

import cats.effect.IO
import cats.data.EitherT

object Scenario2 {

  class DatabaseUser(credentials: DatabaseCredentials) {

    def obtain: EitherT[IO, Exception, DatabaseConnection] =
      eitherTWrapper(DatabaseConnection.open(credentials))

    def put(
        connection: DatabaseConnection,
        datum: Datum
    ): EitherT[IO, Exception, Unit] =
      eitherTWrapper(connection.put(datum.key, datum.serializeContent))

    def getAll(
        connection: DatabaseConnection
    ): EitherT[IO, Exception, List[Datum]] = {

      def helper(
          keys: List[String],
          acc: List[Datum] = List()
      ): EitherT[IO, Exception, List[Datum]] = keys match {
        case head :: tail => {
          for {
            bytes <- eitherTWrapper(connection.fetch(head))
            datum <- eitherTWrapper(Datum.deserialize(bytes))
            value <- helper(tail, datum +: acc)
          } yield value
        }
        case Nil =>
          EitherT[IO, Exception, List[Datum]] {
            IO.pure(Right(acc))
          }
      }

      eitherTWrapper(connection.keys.toList).flatMap { keys =>
        helper(keys)
      }
    }

    def close(connection: DatabaseConnection): EitherT[IO, Exception, Unit] =
      eitherTWrapper(connection.close())
  }
}
