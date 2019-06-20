package io.github.dector.glow.pipeline

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class PipelinedGlowEngineTest : BehaviorSpec({

    Given("empty pipeline") {
        val engine = PipelinedGlowEngine(
            pipeline = EmptyPipeline()
        )

        When("it is executed") {
            val result = engine.execute()

            Then("execution should be success") {
                result.isSuccess shouldBe true
            }
        }
    }

    Given("empty pipeline that throws error") {
        val engine = PipelinedGlowEngine(
            pipeline = ErrorPipeline()
        )

        When("it is executed") {
            val result = engine.execute()

            Then("execution should be failure") {
                result.isSuccess shouldBe false
            }
        }
    }
})

private class EmptyPipeline : GlowPipeline {

    override fun execute() {}
}

private class ErrorPipeline : GlowPipeline {

    override fun execute() {
        throw object : Throwable() {}
    }
}
