package com.swordfish.touchinput.radial.layouts

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.swordfish.touchinput.radial.controls.LemuroidControlButton
import com.swordfish.touchinput.radial.controls.LemuroidControlCross
import com.swordfish.touchinput.radial.controls.LemuroidControlFaceButtons
import com.swordfish.touchinput.radial.layouts.shared.ComposeTouchLayouts
import com.swordfish.touchinput.radial.layouts.shared.SecondaryButtonMenu
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import com.swordfish.touchinput.radial.ui.LemuroidButtonForeground
import com.swordfish.touchinput.radial.utils.buildCentral6ButtonsAnchors
import gg.padkit.PadKitScope
import gg.padkit.anchors.Anchor
import gg.padkit.ids.Id
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentMapOf

/**
 * PC-FX Layout.
 * 
 * Button Mapping (beetle-pcfx-libretro core):
 * - B → II
 * - A → I  
 * - X → III
 * - Y → IV
 * - L → V
 * - R → VI
 * - L2 → MODE 1
 * - R2 → MODE 2
 * - SELECT → Select
 * - START → Run
 */

@Composable
fun PadKitScope.PCFXLeft(
    modifier: Modifier = Modifier,
    settings: TouchControllerSettingsManager.Settings,
) {
    BaseLayoutLeft(
        settings = settings,
        modifier = modifier,
        primaryDial = {
            LemuroidControlCross(
                id = Id.DiscreteDirection(ComposeTouchLayouts.MOTION_SOURCE_DPAD),
                buttonId = "dpad",
                settings = settings,
                applyGlobalScale = true
            )
        },
        secondaryDials = {
            // Select
            LemuroidControlButton(
                modifier = Modifier.radialPosition(120f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_SELECT),
                label = "Sel",
                settings = settings,
                buttonId = "select_button"
            )
            // Run (Start)
            LemuroidControlButton(
                modifier = Modifier.radialPosition(160f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_START),
                label = "Run",
                settings = settings,
                buttonId = "start_button"
            )
            // Mode 1 (L2)
            LemuroidControlButton(
                modifier = Modifier.radialPosition(140f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_L2),
                label = "M1",
                settings = settings,
                buttonId = "l2_button"
            )
        },
    )
}

@Composable
fun PadKitScope.PCFXRight(
    modifier: Modifier = Modifier,
    settings: TouchControllerSettingsManager.Settings,
) {
    // Face Buttons: 
    // I (Right) → A
    // II (Bottom) → B
    // III (Top) → X
    // IV (Left) → Y

    val centralAnchors = rememberCentralAnchorsForPCFX(settings.rotation)

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
                    persistentMapOf(
                        Id.Key(KeyEvent.KEYCODE_BUTTON_X) to { // Top = III
                             LemuroidButtonForeground(pressed = it, label = "III")
                        }, 
                        Id.Key(KeyEvent.KEYCODE_BUTTON_Y) to { // Left = IV
                             LemuroidButtonForeground(pressed = it, label = "IV")
                        },  
                        Id.Key(KeyEvent.KEYCODE_BUTTON_A) to { // Right = I
                             LemuroidButtonForeground(pressed = it, label = "I")
                        },       
                        Id.Key(KeyEvent.KEYCODE_BUTTON_B) to { // Bottom = II
                             LemuroidButtonForeground(pressed = it, label = "II")
                        }       
                    ),
                settings = settings,
                buttonId = "face_buttons_pcfx",
                applyGlobalScale = true
            )
        },
        secondaryDials = {
            // VI (R)
            LemuroidControlButton(
                modifier = Modifier.radialPosition(-45f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_R1),
                label = "VI",
                settings = settings,
                buttonId = "r1_button"
            )
            // V (L)
            LemuroidControlButton(
                modifier = Modifier.radialPosition(45f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_L1),
                label = "V",
                settings = settings,
                buttonId = "l1_button"
            )
            // Mode 2 (R2)
            LemuroidControlButton(
                modifier = Modifier.radialPosition(0f),
                id = Id.Key(KeyEvent.KEYCODE_BUTTON_R2),
                label = "M2",
                settings = settings,
                buttonId = "r2_button"
            )
            SecondaryButtonMenu(settings)
        },
    )
}

@Composable
private fun rememberCentralAnchorsForPCFX(rotation: Float): PersistentList<Anchor<Id.Key>> {
    return remember(rotation) {
        // Map anchors to proper buttons:
        // Top=X (III), Left=Y (IV), Right=A (I), Bottom=B (II)
        buildCentral6ButtonsAnchors(
            rotation,
            KeyEvent.KEYCODE_BUTTON_X,  // Top = III
            KeyEvent.KEYCODE_BUTTON_Y,  // Left = IV
            KeyEvent.KEYCODE_BUTTON_A,  // Right = I
            KeyEvent.KEYCODE_BUTTON_B   // Bottom = II
        )
    }
}
