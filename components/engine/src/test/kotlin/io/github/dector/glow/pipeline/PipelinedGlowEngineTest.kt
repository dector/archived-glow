package space.dector.glow.pipeline

import io.kotlintest.shouldThrowAny
import io.kotlintest.specs.BehaviorSpec
import space.dector.glow.engine.GlowEngine
import space.dector.glow.engine.GlowPipeline

class PipelinedGlowEngineTest : BehaviorSpec({

    Given("empty pipeline") {
        val engine = GlowEngine(
            EmptyPipeline()
        )

        When("it is executed") {
            engine.execute()
        }
    }

    Given("empty pipeline that throws error") {
        val engine = GlowEngine(
            ErrorPipeline()
        )

        When("it is executed") {
            val action = { engine.execute() }

            Then("execution should be failure") {
                shouldThrowAny(action)
            }
        }
    }
})

private class EmptyPipeline : GlowPipeline {

    override fun onExecute() {}
}

private class ErrorPipeline : GlowPipeline {

    override fun onExecute() {
        throw object : Throwable() {}
    }
}
