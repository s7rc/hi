package com.swordfish.touchinput.radial.controls

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.alpha
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme
import com.swordfish.touchinput.radial.ui.LemuroidControlBackground
import com.swordfish.touchinput.radial.ui.LemuroidCrossForeground
import gg.padkit.PadKitScope
import gg.padkit.controls.ControlCross
import gg.padkit.ids.Id
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import com.swordfish.touchinput.radial.layouts.ControlEditMode

context(PadKitScope)
@Composable
fun LemuroidControlCross(
    modifier: Modifier = Modifier,
    id: Id.DiscreteDirection,
    allowDiagonals: Boolean = true,
    background: @Composable () -> Unit = {
        LemuroidControlBackground()
    },
    foreground: @Composable (State<Offset>) -> Unit = {
        LemuroidCrossForeground(
            allowDiagonals = allowDiagonals,
            directionState = if (ControlEditMode.isEditMode) androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(Offset.Zero) } else it,
        )
    },
    settings: TouchControllerSettingsManager.Settings? = null,
    buttonId: String? = "dpad",
    applyGlobalScale: Boolean = false,
) {
    val theme = LocalLemuroidPadTheme.current
    val content = @Composable {
        ControlCross(
            modifier = modifier.padding(theme.padding),
            id = id,
            allowDiagonals = allowDiagonals,
            background = background,
            foreground = foreground,
        )
    }

    if (settings != null && buttonId != null) {
        com.swordfish.touchinput.radial.layouts.CustomizableControl(
            buttonId = buttonId,
            settings = settings,
            modifier = modifier,
            applyGlobalScale = applyGlobalScale
        ) {
            ControlCross(
                modifier = Modifier.padding(theme.padding),
                id = id,
                allowDiagonals = allowDiagonals,
                background = {
                    background()
                },
                foreground = {
                    foreground(it)
                },
            )
        }
    } else {
        content()
    }
}
