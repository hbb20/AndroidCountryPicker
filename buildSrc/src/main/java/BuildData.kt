object BuildData {
    const val demoApplicationId = "com.hbb20.androidcountrypicker"

    // build version
    private const val majorVersionNumber = 0
    private const val minorVersionNumber = 0
    private const val patchVersionNumber = 1
    val versionName = makeVersionName(majorVersionNumber, minorVersionNumber, patchVersionNumber)
    val versionCode = makeVersionCode(majorVersionNumber, minorVersionNumber, patchVersionNumber)

    // SDK versions
    const val minSdkVersion = 23
    const val targetSdkVersion = 33
    const val compileSdkVersion = 33
}