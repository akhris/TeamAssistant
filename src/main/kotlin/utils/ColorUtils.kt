package utils

import androidx.compose.ui.graphics.Color

fun Color.oppositeColor(bw: Boolean = true): Color {

    val y = red * 0.299 + green * 0.587 + blue * 0.114
    if (bw) {
        return if (y > 0.730) {
            Color.Black
        } else {
            Color.White
        }
    }

    return Color(255 - red, 255 - green, 255 - blue, alpha)
}
