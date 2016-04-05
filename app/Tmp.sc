class Foo{

  def print =println("foo")
}

class Bar extends Foo{
  override def print: Unit = println("bar")
}

val bar = new Bar

bar.print

val foo: Foo = bar
foo.print