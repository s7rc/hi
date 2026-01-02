package com.swordfish.touchinput.radial.layouts.shared

import android.view.KeyEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swordfish.touchinput.controller.R
import com.swordfish.touchinput.radial.controls.LemuroidControlAnalog
import com.swordfish.touchinput.radial.controls.LemuroidControlButton
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import gg.padkit.PadKitScope
import gg.padkit.ids.Id
import gg.padkit.layouts.radial.secondarydials.LayoutRadialSecondaryDialsScope

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonSelect(
    settings: TouchControllerSettingsManager.Settings? = null,
    position: Int = 0
) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(120f - 30f * position),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_SELECT),
        icon = R.drawable.button_select,
        settings = settings,
        buttonId = "select_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonL1(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(90f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_L1),
        label = "L1",
        settings = settings,
        buttonId = "l1_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonL2(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(120f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_L2),
        label = "L2",
        settings = settings,
        buttonId = "l2_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonR1(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(90f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_R1),
        label = "R1",
        settings = settings,
        buttonId = "r1_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonR2(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(60f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_R2),
        label = "R2",
        settings = settings,
        buttonId = "r2_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonL(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(120f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_L1),
        label = "L",
        settings = settings,
        buttonId = "l_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonR(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(60f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_R1),
        label = "R",
        settings = settings,
        buttonId = "r_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonStart(
    settings: TouchControllerSettingsManager.Settings? = null,
    position: Int = 0
) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(60f + 30f * position),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_START),
        icon = R.drawable.button_start,
        settings = settings,
        buttonId = "start_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonMenu(settings: TouchControllerSettingsManager.Settings) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(-60f + 2f * settings.rotation * TouchControllerSettingsManager.MAX_ROTATION),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_MODE),
        icon = R.drawable.button_menu,
        settings = settings,
        buttonId = "menu_button"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonMenuPlaceholder(settings: TouchControllerSettingsManager.Settings) {
    Box(
        modifier =
            Modifier.radialPosition(
                -120f - 2f * settings.rotation * TouchControllerSettingsManager.MAX_ROTATION,
            ),
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryAnalogLeft(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlAnalog(
        modifier =
            Modifier
                .radialPosition(-80f)
                .radialScale(2.0f),
        id = Id.ContinuousDirection(ComposeTouchLayouts.MOTION_SOURCE_LEFT_STICK),
        analogPressId = Id.Key(KeyEvent.KEYCODE_BUTTON_THUMBL),
        settings = settings,
        buttonId = "analog_left"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryAnalogRight(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlAnalog(
        modifier =
            Modifier
                .radialPosition(+80f - 180f)
                .radialScale(2.0f),
        id = Id.ContinuousDirection(ComposeTouchLayouts.MOTION_SOURCE_RIGHT_STICK),
        analogPressId = Id.Key(KeyEvent.KEYCODE_BUTTON_THUMBR),
        settings = settings,
        buttonId = "analog_right"
    )
}

context(PadKitScope, LayoutRadialSecondaryDialsScope)
@Composable
fun SecondaryButtonCoin(settings: TouchControllerSettingsManager.Settings? = null) {
    LemuroidControlButton(
        modifier = Modifier.radialPosition(120f),
        id = Id.Key(KeyEvent.KEYCODE_BUTTON_SELECT),
        icon = R.drawable.button_coin,
        settings = settings,
        buttonId = "coin_button"
    )
}

object ComposeTouchLayouts {
    const val MOTION_SOURCE_DPAD = 0
    const val MOTION_SOURCE_LEFT_STICK = 1
    const val MOTION_SOURCE_RIGHT_STICK = 2
    const val MOTION_SOURCE_DPAD_AND_LEFT_STICK = 3
    const val MOTION_SOURCE_RIGHT_DPAD = 4
}
