package hydra.avro

import java.util.UUID

import hydra.avro.convert.{UUIDConverter, UUIDType}
import org.apache.avro.LogicalTypes.LogicalTypeFactory
import org.apache.avro.{LogicalType, LogicalTypes, Schema}
import org.scalatest.{FlatSpecLike, Matchers}

class UUIDConverterSpec extends Matchers with FlatSpecLike {

  LogicalTypes.register(UUIDType.UuidLogicalTypeName, new LogicalTypeFactory {
    override def fromSchema(schema: Schema): LogicalType = UUIDType
  })

  "The UUIDConverter class" should "convert UUIDs" in {
    val c = new UUIDConverter()
    c.getConvertedType shouldBe classOf[UUID]
    c.getLogicalTypeName shouldBe "uuid"
    val uuid = c.fromCharSequence("64b97274-c85b-4d0d-badb-d6ec918339c7",
      Schema.create(Schema.Type.STRING), UUIDType)

    uuid shouldBe UUID.fromString("64b97274-c85b-4d0d-badb-d6ec918339c7")
  }


  it should "use the logical type when parsing a schema" in {

    val schemaStr =
      """
        |{
        |  "namespace": "hydra.test",
        |  "type": "record",
        |  "name": "Uuid",
        |  "fields": [
        |    {
        |      "name": "userId",
        |      "type":{
        |        "type": "string",
        |        "logicalType":"uuid"
        |      }
        |    }
        |  ]
        |}
      """.stripMargin

    val schema = new Schema.Parser().parse(schemaStr)
    schema.getField("userId").schema().getLogicalType.getName shouldBe UUIDType.UuidLogicalTypeName
  }

  "The UUID LogicalType" should "validate correct types" in {
    UUIDType.validate(Schema.create(Schema.Type.STRING))
    intercept[IllegalArgumentException] {
      UUIDType.validate(Schema.create(Schema.Type.INT))
    }
  }

}
