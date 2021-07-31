/**
 * Format <major version>.<minor version>.<patch version>
 */
fun makeVersionName(
    majorVersionNumber: Int,
    minorVersionNumber: Int,
    patchVersionNumber: Int
): String {
    if (minorVersionNumber >= 100) throw IllegalArgumentException("Minor version number can not be more than 2 digits.")
    if (patchVersionNumber >= 100) throw IllegalArgumentException("Patch version number can not be more than 2 digits.")
    return "$majorVersionNumber.$minorVersionNumber.$patchVersionNumber"
}

/**
 * Last two digits = patch
 * Next two digits = minor release
 * Remaining digits = major version
 *
 * For example, if major = 2, minor = 5 and patch = 12
 * then version code will be 20512
 */
fun makeVersionCode(
    majorVersionNumber: Int,
    minorVersionNumber: Int,
    patchVersionNumber: Int
): Int {
    if (minorVersionNumber >= 100) throw IllegalArgumentException("Minor version number can not be more than 2 digits.")
    if (patchVersionNumber >= 100) throw IllegalArgumentException("Patch version number can not be more than 2 digits.")
    return majorVersionNumber * 10000 + minorVersionNumber * 100 + patchVersionNumber
}