import java.io.File

data class DependencyUpdate(val id: String, val currentVersion: String, val availableLatestVersion: String)

fun autoUpdateDependencies() {
    val fileLines = readFileLines()
    fileLines.filterIsInstance<FileLine.DependencyLine>().forEach { println(it) }
    val depsMapById =
        fileLines.filterIsInstance(FileLine.DependencyLine::class.java).associateBy { it.depId }
    depsMapById.entries.forEach { print(it) }
    val depVariableByName = fileLines.filterIsInstance(FileLine.VersionVariableLine::class.java)
        .associateBy { it.variableName }
    val availableUpdates = getAvailableUpdates()
    availableUpdates.forEach { print(it) }

    // fill in available updates
    // if implementation uses a variable then put update version only for the associate variable line
    availableUpdates.forEach { update ->
        val depline = depsMapById[update.id]
        if (depline == null) {
            println("Could not find line for ${update.id} so did not update ")
        } else if (depline.isUpdateBlocked.not()) {
            if (depline.associatedVersionVarName != null) {
                depVariableByName[depline.associatedVersionVarName]?.updateVersion =
                    update.availableLatestVersion
            } else {
                depline.updateVersion = update.availableLatestVersion
            }
        }
    }

    // now add update lines
    fileLines.forEach { line ->
        println(line)
        if (line is FileLine.VersionVariableLine && line.updateVersion != null && line.isUpdateBlocked.not()) {
            line.updateLine =
                line.originalText.replace("\"${line.currentVersion}\"", "\"${line.updateVersion}\"")
        } else if (line is FileLine.DependencyLine && line.updateVersion != null && line.isUpdateBlocked.not() && line.associatedVersionVarName == null) {
            line.updateLine =
                line.originalText.replace("${line.definedVersion}\"", "${line.updateVersion}\"")
        }
    }

    getDepsKt().apply {
        writeText(fileLines.joinToString("\n") { it.finalTextToWrite() })
        appendText("\n")
    }
}

private fun getDepsKt(): File = File("./buildSrc/src/main/java/Dependencies.kt")

private fun getAvailableUpdates(reportFile: File = File("build/dependencyUpdates/report.txt")): MutableList<DependencyUpdate> {
    val updateLinePattern = """^ - .+:.+ \[.+ -> .+]""".toRegex() // https://regex101.com/r/sRM7k1/1
    val updateLines = reportFile.readLines().filter { updateLinePattern.matches(it) }
    val updates = mutableListOf<DependencyUpdate>()
    updateLines.forEach {
        val lineParts = it.removePrefix(" - ").split(" ", limit = 2)
        val depId = lineParts[0]
        val versions = lineParts[1].trim().removeSuffix("]").removePrefix("[").split(" -> ")
        updates.add(DependencyUpdate(depId, versions[0], versions[1]))
    }
    return updates
}

private fun readFileLines(depskt: File = getDepsKt()): List<FileLine> {
    val result = mutableListOf<FileLine>()
    depskt.readLines().forEach { line ->
        val sizeAtStart = result.size
        line.parseAsVersionVariableLine()?.let {
            result.add(it)
        }

        line.parseAsDepVariableLine()?.let {
            result.add(it)
        }

        line.parseAsDepImplementationLine()?.let {
            result.add(it)
        }

        if (sizeAtStart == result.size) {
            result.add(FileLine.OtherLine(line, line.isDepUpdateBlocked()))
        }
    }
    return result
}

private sealed class FileLine(
    val originalText: String,
    val isUpdateBlocked: Boolean
) {
    abstract fun finalTextToWrite(): String
    data class VersionVariableLine(
        val _originalText: String,
        val _isUpdateBlocked: Boolean,
        val variableName: String,
        val currentVersion: String,
        var updateVersion: String? = null,
        var updateLine: String? = null

    ) : FileLine(_originalText, _isUpdateBlocked) {
        override fun finalTextToWrite(): String {
            return updateLine ?: originalText
        }
    }

    data class DependencyLine(
        val _originalText: String,
        val _isUpdateBlocked: Boolean,
        val depId: String,
        val definedVersion: String,
        val associatedVersionVarName: String? = null,
        var updateVersion: String? = null,
        var updateLine: String? = null
    ) : FileLine(_originalText, _isUpdateBlocked) {
        override fun finalTextToWrite(): String {
            return updateLine ?: originalText
        }
    }

    data class OtherLine(
        val _originalText: String,
        val _isUpdateBlocked: Boolean
    ) : FileLine(_originalText, _isUpdateBlocked) {
        override fun finalTextToWrite(): String {
            return originalText
        }
    }
}

private data class DeclaredVariable(val name: String, val value: String)

/**
 * If line is variable for dependency VERSION like
 *     const val MOSHI_SEALED = "0.2.0"
 */
private fun String.parseAsVersionVariableLine(): FileLine.VersionVariableLine? {
    var result: FileLine.VersionVariableLine? = null
    parseDeclaredVariable()?.let {
        val versionPattern = """^\d+\.\d+(\..*)?""".toRegex()
        if (versionPattern.matches(it.value)) {
            result = FileLine.VersionVariableLine(
                this,
                isDepUpdateBlocked(),
                variableName = it.name,
                currentVersion = it.value
            )
        }
    }
    return result
}

/**
 * If line is variable for dependency variable like
 *     const val coil = "io.coil-kt:coil:1.1.1"
 */
private fun String.parseAsDepVariableLine(): FileLine.DependencyLine? {
    var result: FileLine.DependencyLine? = null
    parseDeclaredVariable()?.let {
        val depPattern = """(.+:.+):(.+)""".toRegex()
        depPattern.find(it.value)?.let { matchResult ->
            val (depId, version) = matchResult.destructured
            result = FileLine.DependencyLine(
                this,
                isDepUpdateBlocked(),
                depId = depId,
                definedVersion = version,
                associatedVersionVarName = findEmbeddedVersionVarName()
            )
        }
    }
    return result
}

private fun String.findEmbeddedVersionVarName(): String? {
    val embeddedVersionPattern = "\\\$\\{Versions.(.+)\\}".toRegex()
//    val embeddedVersionPattern = """\${'$'}\{Versions\.(.+)\}""".toRegex()
    val embeddedVersionMatch = embeddedVersionPattern.find(this)
    var versionVariableName: String? = null
    if (embeddedVersionMatch != null) {
        versionVariableName = embeddedVersionMatch.destructured.component1()
    }
    return versionVariableName
}

/**
 * If line is variable for dependency implementation like
 *         add(IMPLEMENTATION, "com.github.yalantis:ucrop:2.2.6")
 */
private fun String.parseAsDepImplementationLine(): FileLine.DependencyLine? {
    var result: FileLine.DependencyLine? = null
    val depImplementationPattern = """add\(.+, "(.+:.+):(.+)"""".toRegex()
    depImplementationPattern.find(this)?.let { matchResult ->
        val (depId, version) = matchResult.destructured
        result = FileLine.DependencyLine(
            this,
            isDepUpdateBlocked(),
            depId = depId,
            definedVersion = version,
            associatedVersionVarName = findEmbeddedVersionVarName()
        )
    }
    return result
}

private fun String.isDepUpdateBlocked(): Boolean {
    val indexOfComment = indexOf("//")
    val indexOfStop = indexOf("stop", ignoreCase = true)
    return indexOfStop > indexOfComment
}

private fun String.parseDeclaredVariable(): DeclaredVariable? {
    var result: DeclaredVariable? = null
    //  https://regex101.com/r/2wM3Tt/1
    val declarationPattern = """val ([a-zA-Z0-9_]+) = "(.+)"""".toRegex()
    declarationPattern.find(this)?.let { matchResult ->
        val (variableName, variableValue) = matchResult.destructured
        result = DeclaredVariable(variableName, variableValue)
    }
    return result
}
