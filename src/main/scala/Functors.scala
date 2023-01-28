import scala.util.Try

object Functors {
  val aModifiedList = List(1, 2, 3).map(_ + 1)
  val aModifiedOption = Option(1).map(_ + 1)
  val aModifiedTry = Try(1).map(_ + 1)

  trait MyFunctor[F[_]] {
    def map[A, B](initialValue: F[A])(f: A => B): F[B]
  }

  import cats.Functor
  import cats.instances.list._

  val listFunctor = Functor[List]
  val incrementedNumbers = listFunctor.map(List(1,2,3))(_ + 1)

  import cats.instances.option._
  val optionFunctor = Functor[Option]
  val incrementedOption = optionFunctor.map(Option(2))(_ + 1)

  import cats.instances.try_._
  val anIncrementedTry = Functor[Try].map(Try(32))(_ + 1)
  import cats.syntax.functor._
  def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] = functor.map(container)(_ * 10)
  def do10xShorter[F[_]: Functor](container: F[Int]): F[Int] = container.map(_ * 10)

  trait Tree[+T]
  object Tree {
    def leaf[T](value: T) = Leaf(value)
    def branch[T](value: T, left: Tree[T], right: Tree[T]): Tree[T] = Branch(value, left, right)
  }
  case class Leaf[+T](value: T) extends Tree[T]
  case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

  implicit object TreeFunctor extends Functor[Tree]{
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Branch(v, left, right) => Branch(f(v), map(left)(f), map(right)(f))
      case Leaf(value) => Leaf(f(value))
    }
  }

  val tree: Tree[Int] = Tree.branch(40, Tree.branch(4, Tree.leaf(20), Tree.leaf(20)), Tree.leaf(20))
  val incrementedTree = tree.map(_ + 1)
  def main(args: Array[String]): Unit = {
    println(do10x(anIncrementedTry))
    val branch: Branch[Int] = Branch[Int](30, Tree.leaf(20), Tree.branch(10,Tree.leaf(120), Tree.leaf(20)))
    println(do10x[Tree](branch))
    println(do10xShorter[Tree](branch))
  }
}