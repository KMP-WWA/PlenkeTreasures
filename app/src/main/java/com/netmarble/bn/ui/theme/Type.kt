package com.netmarble.bn.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.netmarble.bn.R

val Marcellus = FontFamily(
    Font(R.font.marcellus_regular, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Marcellus,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)