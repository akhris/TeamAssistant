package utils

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}

fun <T> MutableList<T>.replace(newValue: T, block: (T) -> Boolean) {
    val oldItem = this.find { block(it) } ?: return
    val oldItemPos = indexOf(oldItem)
    this[oldItemPos] = newValue
}