package com.swordfish.touchinput.radial.controls

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme
import com.swordfish.touchinput.radial.ui.LemuroidCompositeForeground
import com.swordfish.touchinput.radial.ui.LemuroidControlBackground
import gg.padkit.PadKitScope
import gg.padkit.anchors.Anchor
import gg.padkit.controls.ControlFaceButtons
import gg.padkit.ids.Id
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import com.swordfish.touchinput.radial.layouts.ControlEditMode
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import gg.padkit.controls.ControlButton
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.ui.draw.alpha

context(PadKitScope)
@Composable
fun LemuroidControlFaceButtons(
    modifier: Modifier = Modifier,
    rotationInDegrees: Float = 0f,
    ids: PersistentList<Id.Key>,
    includeComposite: Boolean = true,
    applyPadding: Boolean = true,
    trackPointers: Boolean = true,
    background: @Composable () -> Unit = { LemuroidControlBackground() },
    idsForegrounds: PersistentMap<Id.Key, @Composable (State<Boolean>) -> Unit>,
    settings: TouchControllerSettingsManager.Settings? = null,
    buttonId: String? = "face_buttons",
    applyGlobalScale: Boolean = false,
) {
    val theme = LocalLemuroidPadTheme.current
    val content = @Composable {
        ControlFaceButtons(
            modifier =
            modifier
                .run { if (applyPadding) padding(theme.padding) else modifier },
            includeComposite = includeComposite,
            ids = ids,
            trackPointers = trackPointers,
            rotationInDegrees = rotationInDegrees,
            foreground = { id, pressed -> 
                val effectivePressed = if (com.swordfish.touchinput.radial.layouts.ControlEditMode.isEditMode) androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) } else pressed
                (idsForegrounds[id]!!)(effectivePressed) 
            },
            background = background,
            foregroundComposite = { LemuroidCompositeForeground(it) },
        )
    }

    if (settings != null && buttonId != null) {
        com.swordfish.touchinput.radial.layouts.CustomizableControl(
            buttonId = buttonId,
            settings = settings,
            modifier = modifier,
            applyGlobalScale = applyGlobalScale
        ) {
            ControlFaceButtons(
                modifier = if (applyPadding) Modifier.padding(theme.padding) else Modifier,
                includeComposite = includeComposite,
                ids = ids,
                trackPointers = trackPointers,
                rotationInDegrees = rotationInDegrees,
                foreground = { id, pressed -> 
                    val effectivePressed = if (com.swordfish.touchinput.radial.layouts.ControlEditMode.isEditMode) androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) } else pressed
                    (idsForegrounds[id]!!)(effectivePressed)
                },
                background = {
                    background()
                },
                foregroundComposite = { 
                    LemuroidCompositeForeground(it)
                },
            )
        }
    } else {
        content()
    }
}

context(PadKitScope)
@Composable
fun LemuroidControlFaceButtons(
    modifier: Modifier = Modifier,
    primaryAnchors: PersistentList<Anchor<Id.Key>>,
    background: @Composable () -> Unit = { LemuroidControlBackground() },
    applyPadding: Boolean = true,
    trackPointers: Boolean = true,
    idsForegrounds: PersistentMap<Id.Key, @Composable (State<Boolean>) -> Unit>,
    settings: TouchControllerSettingsManager.Settings? = null,
    buttonId: String? = "face_buttons",
    applyGlobalScale: Boolean = true,
) {
    val theme = LocalLemuroidPadTheme.current
    val content = @Composable {
        ControlFaceButtons(
            modifier =
            modifier
                .run { if (applyPadding) padding(theme.padding) else modifier },
            primaryAnchors = primaryAnchors,
            compositeAnchors = persistentListOf(),
            trackPointers = trackPointers,
            foreground = { id, pressed -> 
                val effectivePressed = if (com.swordfish.touchinput.radial.layouts.ControlEditMode.isEditMode) androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) } else pressed
                (idsForegrounds[id]!!)(effectivePressed) 
            },
            background = background,
            foregroundComposite = { LemuroidCompositeForeground(it) },
        )
    }

    if (settings != null && buttonId != null) {
         com.swordfish.touchinput.radial.layouts.CustomizableControl(
            buttonId = buttonId,
            settings = settings,
            modifier = modifier,
            applyGlobalScale = applyGlobalScale
        ) {
            ControlFaceButtons(
                modifier = if (applyPadding) Modifier.padding(theme.padding) else Modifier,
                primaryAnchors = primaryAnchors,
                compositeAnchors = kotlinx.collections.immutable.persistentListOf(),
                trackPointers = trackPointers,
                foreground = { id: Id.Key, pressed: androidx.compose.runtime.State<Boolean> ->
                    val effectivePressed = if (ControlEditMode.isEditMode) remember { mutableStateOf(false) } else pressed
                    (idsForegrounds[id]!!)(effectivePressed)
                },
                background = {
                        background()
                },
                foregroundComposite = { 
                        LemuroidCompositeForeground(it)
                },
            )
        }
    } else {
        content()
    }
}
