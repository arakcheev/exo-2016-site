import org.joda.time.DateTime

class Foo {

  def print = println("foo")
}

class Bar extends Foo {
  override def print: Unit = println("bar")
}

val bar = new Bar

bar.print
val foo: Foo = bar
foo.print
trait Item extends{
  val date: DateTime

}

implicit  object Ord extends Ordering[Item]{
  override def compare(x: Item, y: Item): Int = x.date.compareTo(y.date)
}

case class One(d: DateTime) extends Item{
  override val date: DateTime = d
}

case class Two(date: DateTime) extends Item

val ss = collection.mutable.TreeSet.empty[Item]

ss += One(new DateTime())
ss += Two(new DateTime().plusHours(2))
ss += Two(new DateTime().plusDays(6))
ss += One(new DateTime().plusDays(1))
ss += One(new DateTime().plusDays(5))
println(ss)
