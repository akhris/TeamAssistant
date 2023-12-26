package ui

sealed class SelectMode {
    object NONSELECTABLE : SelectMode()
    object SINGLE : SelectMode()
    object MULTIPLE : SelectMode()
}