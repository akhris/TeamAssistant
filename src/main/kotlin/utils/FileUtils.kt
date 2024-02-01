package utils

object FileUtils {
    fun isPathValid(path: String, extensions: List<String> = listOf()): Boolean {
        val regex = if (extensions.isEmpty()) {
            Regex("^/([A-z0-9-_+.]+/)*([A-z0-9.]+)")
        } else {
            Regex("^/([A-z0-9-_+.]+/)*([A-z0-9.]+.${extensions.joinToString(separator = "|")})")
        }
        return path.matches(regex)
    }
}