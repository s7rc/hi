package com.swordfish.touchinput.radial.layouts

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.swordfish.touchinput.radial.controls.LemuroidControlButton
import com.swordfish.touchinput.radial.controls.LemuroidControlCross
import com.swordfish.touchinput.radial.controls.LemuroidControlFaceButtons
import com.swordfish.touchinput.radial.layouts.shared.ComposeTouchLayouts
import com.swordfish.touchinput.radial.layouts.shared.SecondaryButtonMenu
import com.swordfish.touchinput.radial.layouts.shared.SecondaryButtonMenuPlaceholder
import com.swordfish.touchinput.radial.layouts.shared.SecondaryButtonSelect
import com.swordfish.touchinput.radial.layouts.shared.SecondaryButtonStart
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import com.swordfish.touchinput.radial.ui.LemuroidCentralButton
import com.swordfish.touchinput.radial.ui.LemuroidButtonForeground
import com.swordfish.touchinput.radial.utils.buildCentral6ButtonsAnchors
import gg.padkit.PadKitScope
import gg.padkit.anchors.Anchor
import gg.padkit.ids.Id
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun PadKitScope.SaturnLeft(
    modifier: Modifier = Modifier,
    settings: TouchControllerSettingsManager.Settings,
) {
    BaseLayoutLeft(
        settings = settings,
        modifier = modifier,
        primaryDial = {
            LemuroidControlCross(
                id = Id.DiscreteDirection(ComposeTouchLayouts.MOTION_SOURCE_DPAD),
                settings = settings,
                buttonId = "dpad",
                applyGlobalScale = true
            )
        },
        secondaryDials = {
            LemuroidControlButton(
                modifier = Modifier.radialPosition(120f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_L2), // Using L2 for Saturn L
                label = "L",
                settings = settings,
                buttonId = "l_button"
            )
            SecondaryButtonStart(settings, position = 1) // Start
             // Saturn doesn't usually use Select, but having it is fine. Or maybe remove it.
             // Saturn has Start. Let's keep Start.
             // Maybe Coins/Select for arcade?
             // I'll keep default BaseLayout logic if needed.
             // Genesis6 has Select (pos 0) and Start (pos 1).
             // Saturn usually just Start. Moving Start to pos 0?
             // I'll keep Start at 1 for consistency with Genesis, or move to 0.
             // I'll add SecondaryButtonSelect or keep it out? Metal Jesus Rocks says Saturn has Start.
             // Let's keep Start.
        },
    )
}

@Composable
fun PadKitScope.SaturnRight(
    modifier: Modifier = Modifier,
    settings: TouchControllerSettingsManager.Settings,
) {
    val centralAnchors = rememberCentralAnchorsForSixButtons(settings.rotation)

    BaseLayoutRight(
        settings = settings,
        modifier = modifier,
        primaryDial = {
            LemuroidControlFaceButtons(
                primaryAnchors = centralAnchors,
                background = { },
                applyPadding = false,
                trackPointers = false,
                idsForegrounds =
                    persistentMapOf<Id.Key, @Composable (State<Boolean>) -> Unit>(
                        Id.Key(KeyEvent.KEYCODE_BUTTON_L1) to {
                            LemuroidButtonForeground(
                                pressed = it,
                                label = "X",
                            )
                        },
                        Id.Key(KeyEvent.KEYCODE_BUTTON_X) to {
                            LemuroidButtonForeground(
                                pressed = it,
                                label = "Y",
                            )
                        },
                        Id.Key(KeyEvent.KEYCODE_BUTTON_Y) to {
                            LemuroidButtonForeground(
                                pressed = it,
                                label = "A",
                            )
                        },
                        Id.Key(KeyEvent.KEYCODE_BUTTON_B) to {
                            LemuroidButtonForeground(
                                pressed = it,
                                label = "B",
                            )
                        },
                    ),
                settings = settings,
                buttonId = "face_buttons_4_saturn",
                applyGlobalScale = true
            )
        },
        secondaryDials = {
             // Z Button (Top/Right-ish)
             LemuroidControlButton(
                modifier = Modifier.radialPosition(-45f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_R1),
                label = "Z",
                settings = settings,
                buttonId = "z_button"
            )
            // C Button (Bottom/Right-ish)
             LemuroidControlButton(
                modifier = Modifier.radialPosition(45f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_A),
                label = "C",
                settings = settings,
                buttonId = "c_button"
            )
            // R Shoulder (Positioned further out/right)
             LemuroidControlButton(
                modifier = Modifier.radialPosition(0f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_R2),
                label = "R",
                settings = settings,
                buttonId = "r_button"
            )
            SecondaryButtonMenu(settings)
        },
    )
}

@Composable
private fun rememberCentralAnchorsForSixButtons(rotation: Float): PersistentList<Anchor<Id.Key>> {
    return remember(rotation) {
        buildCentral6ButtonsAnchors(
            rotation,
            KeyEvent.KEYCODE_BUTTON_L1, // X
            KeyEvent.KEYCODE_BUTTON_X,  // Y
            KeyEvent.KEYCODE_BUTTON_Y,  // A
            KeyEvent.KEYCODE_BUTTON_B,  // B
        )
    }
}
