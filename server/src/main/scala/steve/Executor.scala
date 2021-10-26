package steve

import cats.Applicative
import cats.implicits.*
import cats.ApplicativeThrow

trait Executor[F[_]] {
  def build(build: Build): F[Hash]
  def run(hash: Hash): F[SystemState]
}

object Executor {
  def apply[F[_]](using F: Executor[F]): Executor[F] = F

  def instance[F[_]: ApplicativeThrow]: Executor[F] = new Executor[F] {
    private val emptyHash: Hash = Hash(Array[Byte]())

    def build(build: Build): F[Hash] =
      (build == Build.empty)
        .guard[Option]
        .as(emptyHash)
        .liftTo[F](new Throwable("Unsupported build!"))

    def run(hash: Hash): F[SystemState] = (hash == emptyHash)
      .guard[Option]
      .as(KVState(Map.empty))
      .liftTo[F](new Throwable("Unsupported hash!"))
  }

  private final case class KVState(map: Map[String, String]) extends SystemState {
    def getAll = map
  }
}

trait SystemState {
  def getAll: Map[String, String]
}
