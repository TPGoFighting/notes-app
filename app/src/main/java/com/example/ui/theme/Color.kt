package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Memphis Design System Palette
val MemphisYellow = Color(0xFFFFE600)
val MemphisCream = Color(0xFFFFF8E7)
val MemphisCreamBg = Color(0xFFFFFDF5)
val MemphisCreamDark = Color(0xFFF5EDD6)
val MemphisInk = Color(0xFF0A0A0A)
val MemphisPink = Color(0xFFFF007F)
val MemphisBlue = Color(0xFF3A86FF)
val MemphisMint = Color(0xFF00F5D4)
val MemphisLilac = Color(0xFFC77DFF)
val MemphisOrange = Color(0xFFFF7F50)
val MemphisRed = Color(0xFFFF4757)

// Pastels for Card Backgrounds
val MemphisPastelBlue = Color(0xFFEBF4FF)
val MemphisPastelPink = Color(0xFFFFEBF3)
val MemphisPastelYellow = Color(0xFFFFF9DB)
val MemphisPastelMint = Color(0xFFE6FAF8)
val MemphisPastelOrange = Color(0xFFFFF0EB)
val MemphisPastelLilac = Color(0xFFF3E8FF)
val MemphisPastelRed = Color(0xFFFFEAEB)

// Accents
val MemphisGridLine = Color(0xFFE8E0CE)
val MemphisWhite = Color(0xFFFFFFFF)
val MemphisBorder = Color(0xFF0A0A0A)

fun parseHexColor(hexString: String, defaultColor: Color = MemphisBlue): Color {
    return try {
        val cleanHex = hexString.trim().removePrefix("#")
        val colorInt = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(0xFF000000 or colorInt)
        } else if (cleanHex.length == 8) {
            Color(colorInt)
        } else {
            defaultColor
        }
    } catch (_: Exception) {
        defaultColor
    }
}
