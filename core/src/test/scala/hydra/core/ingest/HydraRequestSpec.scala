package hydra.core.ingest

import hydra.core.transport.{RetryStrategy, ValidationStrategy}
import org.scalatest.{FunSpecLike, Matchers}

/**
  * Created by alexsilva on 3/22/17.
  */
class HydraRequestSpec extends Matchers with FunSpecLike {

  describe("A HydraRequest") {
    it("return metadata value regardless of case") {
      val hr = HydraRequest(123, metadata = Map("test" -> "value"), payload = "test")
      hr.metadataValue("TEST").get shouldBe "value"
      hr.metadataValue("test").get shouldBe "value"
      hr.metadataValue("TeSt").get shouldBe "value"
      hr.metadataValue("TEST1") shouldBe None
    }

    it("compares metadata values regardless of case") {
      val hr = HydraRequest(123, metadata = Map("test" -> "value"), payload = "test")
      hr.metadataValueEquals("TEST", "value") shouldBe true
      hr.metadataValueEquals("TEST1", "value") shouldBe false
      hr.metadataValueEquals("TEST", "?") shouldBe false
    }

    it("copies correlation id") {
      val hr = HydraRequest(123, metadata = Map("test" -> "value"), payload = "test").withCorrelationId(24)
      hr.correlationId shouldBe 24
      hr.payload shouldBe "test"
    }

    it("copies retry strategy") {
      val hr = HydraRequest(123, metadata = Map("test" -> "value"), payload = "test")
        .withRetryStrategy(RetryStrategy.Persist)
      hr.correlationId shouldBe 123
      hr.payload shouldBe "test"
      hr.retryStrategy shouldBe RetryStrategy.Persist
    }

    it("copies validation strategy") {
      val hr = HydraRequest(123, metadata = Map("test" -> "value"), payload = "test")
        .withValidationStratetegy(ValidationStrategy.Relaxed)
      hr.correlationId shouldBe 123
      hr.payload shouldBe "test"
      hr.validationStrategy shouldBe ValidationStrategy.Relaxed
    }

    it("copies metadata") {
      val hr = HydraRequest(123, metadata = Map("test" -> "value"), payload = "test")
      hr.withMetadata("new" -> "value").metadata shouldBe Map("test" -> "value", "new" -> "value")
      hr.withMetadata("test" -> "newvalue").metadata shouldBe Map("test" -> "newvalue")
    }
  }
}