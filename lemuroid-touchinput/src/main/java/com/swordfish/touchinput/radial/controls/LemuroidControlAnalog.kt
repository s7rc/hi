package com.swordfish.touchinput.radial.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme
import com.swordfish.touchinput.radial.ui.LemuroidButtonForeground
import com.swordfish.touchinput.radial.ui.LemuroidControlBackground
import gg.padkit.PadKitScope
import gg.padkit.controls.ControlAnalog
import gg.padkit.ids.Id
import com.swordfish.touchinput.radial.layouts.ControlEditMode
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

context(PadKitScope)
@Composable
fun LemuroidControlAnalog(
    modifier: Modifier = Modifier,
    analogPressId: Id.Key? = null,
    id: Id.ContinuousDirection,
    settings: com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager.Settings? = null,
    buttonId: String? = null,
    applyGlobalScale: Boolean = true,
) {
    val theme = LocalLemuroidPadTheme.current
    val content = @Composable {
        Box(
            modifier = modifier.padding(theme.padding),
            contentAlignment = Alignment.Center,
        ) {
            ControlAnalog(
                id = id,
                analogPressId = analogPressId,
                background = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LemuroidControlBackground(Modifier.fillMaxSize(0.5f))
                    }
                },
                foreground = { 
                    val effectivePressed = if (ControlEditMode.isEditMode) remember { mutableStateOf(false) } else it
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.fillMaxSize(0.5f)) {
                           LemuroidButtonForeground(pressed = effectivePressed)
                        }
                    }
                },
            )
        }
    }

    if (settings != null && buttonId != null) {
        com.swordfish.touchinput.radial.layouts.CustomizableControl(
            buttonId = buttonId,
            settings = settings,
            modifier = modifier,
            applyGlobalScale = applyGlobalScale
        ) {
            Box(
                modifier = Modifier.padding(theme.padding),
                contentAlignment = Alignment.Center,
            ) {
                ControlAnalog(
                    id = id,
                    analogPressId = analogPressId,
                    background = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            LemuroidControlBackground(Modifier.fillMaxSize(0.5f))
                        }
                    },
                    foreground = { 
                        val effectivePressed = if (ControlEditMode.isEditMode) remember { mutableStateOf(false) } else it
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.fillMaxSize(0.5f)) {
                               LemuroidButtonForeground(pressed = effectivePressed)
                            }
                        }
                    },
                )
            }
        }
    } else {
        content()
    }
}
