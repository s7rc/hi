package com.swordfish.touchinput.radial.controls

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.layout.Box
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme
import com.swordfish.touchinput.radial.ui.LemuroidButtonForeground
import com.swordfish.touchinput.radial.ui.LemuroidControlBackground
import gg.padkit.PadKitScope
import gg.padkit.controls.ControlButton
import gg.padkit.ids.Id
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import gg.padkit.layouts.radial.secondarydials.LayoutRadialSecondaryDialsScope
import com.swordfish.touchinput.radial.layouts.ControlEditMode
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

context(PadKitScope)
@Composable
fun LemuroidControlButton(
    modifier: Modifier = Modifier,
    id: Id.Key,
    label: String? = null,
    icon: Int? = null,
    settings: TouchControllerSettingsManager.Settings? = null,
    buttonId: String? = null, // No default ID, must be provided if settings are present
    applyGlobalScale: Boolean = true,
    customForeground: @Composable ((androidx.compose.runtime.State<Boolean>) -> Unit)? = null,
) {
    val theme = LocalLemuroidPadTheme.current
    val content = @Composable {
        ControlButton(
            modifier = modifier.padding(theme.padding),
            id = id,
            foreground = { 
                val effectivePressed = if (ControlEditMode.isEditMode) remember { mutableStateOf(false) } else it
                if (customForeground != null) {
                    customForeground(effectivePressed)
                } else {
                    LemuroidButtonForeground(pressed = effectivePressed, icon = icon, label = label) 
                }
            },
            background = { LemuroidControlBackground() },
        )
    }

    if (settings != null && buttonId != null) {
        com.swordfish.touchinput.radial.layouts.CustomizableControl(
            buttonId = buttonId,
            settings = settings,
            modifier = modifier,
            applyGlobalScale = applyGlobalScale
        ) {
            ControlButton(
                modifier = Modifier.padding(theme.padding),
                id = id,
                foreground = { 
                    val effectivePressed = if (ControlEditMode.isEditMode) remember { mutableStateOf(false) } else it
                         if (customForeground != null) {
                            customForeground(effectivePressed)
                        } else {
                            LemuroidButtonForeground(pressed = effectivePressed, icon = icon, label = label) 
                        }
                },
                background = {
                         LemuroidControlBackground()
                },
            )
        }
    } else {
        content()
    }
}
