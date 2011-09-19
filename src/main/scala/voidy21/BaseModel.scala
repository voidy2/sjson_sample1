package voidy21

import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import Serializer.SJSON
import dispatch.json._

object ClassUtil {
  def getFields(c: Class[_]): List[String] = 
    if (c == classOf[Object]) Nil
    else getFields(c.getSuperclass) ++
      c.getDeclaredFields.map(_.getName).filter {n => try {
          c.getMethod(n)
          true
      } catch {
        case e: NoSuchMethodException => false
      }
    }.toList
}

trait BaseModel {
  def toJson = tojson(this)

  def fieldNames = ClassUtil.getFields(getClass)

  def fieldMap = fieldNames.map {f => (f, getClass.getMethod(f).invoke(this))}
}

object BaseModel {
  implicit def baseModelFormat[T <: BaseModel]
    (implicit m: ClassManifest[T]) : Format[T] = new Format[T] {

    def writes(o: T) = JsObject(
      o.fieldMap.map{case (k, v) =>
        (JsString(k), JsValue(v))
      }.toList
    )

    def reads(json: JsValue) = json match {
      case JsObject(n) => {
        val c = m.erasure
        val fields = ClassUtil.getFields(c)
        val constructor = c.getConstructors.head
        val types = constructor.getParameterTypes
        val args = (types zip fields).map{case (t, name) => 
          (SJSON.in(n(JsString(name))).asInstanceOf[Any] match {
            case num: scala.math.BigDecimal => t.toString match {
              case "int" => num.toInt.asInstanceOf[java.lang.Integer]
              case "double" => num.toDouble.asInstanceOf[java.lang.Double]
              case "long" => num.toLong.asInstanceOf[java.lang.Long]
              case _ => throw new RuntimeException("JsObject expected")
            }
            case default => default
          }).asInstanceOf[Object]}.toList
        constructor.newInstance(args:_*).asInstanceOf[T]
      }
      case _ => throw new RuntimeException("JsObject expected")
    }
  }

}

