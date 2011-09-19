package voidy21

import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._
import voidy21.BaseModel._

case class Author(
  var firstName: String,
  var lastName: String,
  var email: String
) extends BaseModel

case class Hoge(
  a: String,
  b: Int,
  c: Double,
  d: Long
) extends BaseModel

object Main extends App {
  val author = Author("tanaka", "taro", "tanaka@example.com")
  val json1 = author.toJson
  println(json1)
  println(fromjson[Author](Js("""{"firstName" : "tanaka", "lastName" : "taro", "email" : "tanaka@example.com"}""")))

  val hoge = Hoge("a", 1, 5.5, 10L)
  val json2 = hoge.toJson
  println(json2)
  println(fromjson[Hoge](Js("""{"a" : "a", "b" : 1, "c" : 5.5, "d" : 10}""")))

  val authors = List(
    Author("a", "b", "c@d.e"),
    Author("f", "g", "h@i.j"),
    Author("v", "w", "x@y.z")
  )
  val json3 = tojson(authors)
  println(json3)
  println(fromjson[List[Author]](Js("""[{"firstName" : "a", "lastName" : "b", "email" : "c@d.e"}, {"firstName" : "f", "lastName" : "g", "email" : "h@i.j"}, {"firstName" : "v", "lastName" : "w", "email" : "x@y.z"}]""")))
}

