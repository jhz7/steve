package steve

enum Command {
  case Build(build: steve.Build)
  case Run(hash: Hash)
}

case class Build(
    base: Build.Base,
    commands: List[Build.Command]
)

object Build {

  enum Base {
    case EmptyImage
    case ImageReference(hash: steve.Hash)
  }

  enum Command {
    case Upsert(key: String, value: String)
    case Delete(key: String)
  }

  val empty: Build = Build(Base.EmptyImage, Nil)

}

case class Hash(value: Array[Byte])
