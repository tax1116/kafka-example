import io.gatling.recorder.GatlingRecorder
import io.gatling.recorder.config.RecorderPropertiesBuilder
import scala.Option

object Recorder {
    @Suppress("INACCESSIBLE_TYPE")
    @JvmStatic
    fun main(args: Array<String>) {
        val props = RecorderPropertiesBuilder()
            .simulationsFolder(IDEPathHelper.gradleSourcesDirectory.toString())
            .resourcesFolder(IDEPathHelper.gradleResourcesDirectory.toString())
            .simulationPackage("kr.co.taek.dev.gatling")
            .simulationFormatKotlin()

        GatlingRecorder.fromMap(props.build(), Option.apply(IDEPathHelper.recorderConfigFile))
    }
}
