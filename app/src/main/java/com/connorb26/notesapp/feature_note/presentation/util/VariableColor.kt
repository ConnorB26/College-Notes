package com.connorb26.notesapp.feature_note.presentation.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.connorb26.notesapp.ui.theme.DarkGray
import java.lang.Integer.parseInt

class VariableColor {
    companion object {
        fun getColor(bgColor: Color): Color {
            return if (getLuminance(bgColor) > 130) DarkGray else Color.LightGray
        }

        fun clampLuminance(color: Color): Color {
            val outHSL = FloatArray(3)
            val threshold = 0.4f
            ColorUtils.RGBToHSL((color.red*255).toInt(), (color.green*255).toInt(), (color.blue*255).toInt(), outHSL)
            if(outHSL[2] < threshold) {
                outHSL[2] = threshold
                return Color(ColorUtils.HSLToColor(outHSL))
            }
            return color
        }

        fun setLuminance(color: Color, threshold: Float): Color {
            val outHSL = FloatArray(3)
            ColorUtils.RGBToHSL((color.red*255).toInt(), (color.green*255).toInt(), (color.blue*255).toInt(), outHSL)
            outHSL[2] = 0.4f
            return Color(ColorUtils.HSLToColor(outHSL))
        }

        private fun getLuminance(color: Color): Double {
            val buf = Integer.toHexString(color.toArgb())
            val hex = "#" + buf.substring(buf.length - 6)
            val colorStr: String = if(hex[0] == '#') hex.substring(1, 7) else hex
            val r = parseInt(colorStr.substring(0, 2), 16)
            val g = parseInt(colorStr.substring(2, 4), 16)
            val b = parseInt(colorStr.substring(4, 6), 16)
            return ((r * 0.299) + (g * 0.587) + (b * 0.114))
        }
    }
}