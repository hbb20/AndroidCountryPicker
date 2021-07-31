import java.io.File
import java.util.concurrent.TimeUnit

fun getCurrentGitBranchName(): String {
    return "git rev-parse --abbrev-ref HEAD".runCommand().readText()
}

fun isBitrise(): Boolean {
    return (System.getenv("BITRISE_IO") ?: "false").toBoolean()
}

fun String.runCommand(printOutput: Boolean = true): File {
    val outputFile = createTempFile(suffix = ".txt")

    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .redirectErrorStream(true)
        .start()
    proc.waitFor(60, TimeUnit.SECONDS)
    proc.inputStream.copyTo(outputFile.outputStream())

    if (printOutput) {
        println("\n\nExecuted Command `$this` => ")
        outputFile.readLines().forEach { println(it) }
    }
    return outputFile
}