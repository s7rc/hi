package com.swordfish.touchinput.radial.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme

import androidx.compose.runtime.compositionLocalOf

val LocalLemuroidOpacity = compositionLocalOf { 1f }

@Composable
fun LemuroidControlBackground(modifier: Modifier = Modifier) {
    val theme = LocalLemuroidPadTheme.current
    val opacity = LocalLemuroidOpacity.current
    GlassSurface(
        modifier = modifier.fillMaxSize(),
        fillColor = theme.level1Fill.let { it.copy(alpha = it.alpha * opacity) },
        shadowColor = theme.level1Shadow.let { it.copy(alpha = it.alpha * opacity) },
        shadowWidth = theme.level1ShadowWidth,
    )
}
