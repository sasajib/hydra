package hydra.avro.convert

import java.util.UUID

import hydra.common.logging.LoggingAdapter
import org.apache.avro.{Conversion, LogicalType, Schema}

import scala.util.Try

/**
  * Converts strings in UUID format to a UUID object.
  *
  * If an exception is thrown
  */
class UUIDConverter extends Conversion[UUID] with LoggingAdapter {

  override def getLogicalTypeName: String = UUIDType.UuidLogicalTypeName

  override def getConvertedType: Class[UUID] = classOf[UUID]

  override def fromCharSequence(value: CharSequence,
                                schema: Schema, `type`: LogicalType): UUID = {
    Try(UUID.fromString(value.toString))
      .recover {
        case e: Throwable =>
          log.error(e.getMessage, e)
          UUID.randomUUID() //what should we do?
      }.get
  }
}

object UUIDType extends LogicalType("uuid") {
  val UuidLogicalTypeName = "uuid"

  override def validate(schema: Schema): Unit = {
    if (schema.getType() != Schema.Type.STRING) {
      throw new IllegalArgumentException("uuid can only be used with an underlying string type")
    }
  }
}
