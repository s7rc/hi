package com.swordfish.lemuroid.app.mobile.feature.gamemenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.shared.game.viewmodel.GameViewModelTouchControls
import com.swordfish.lemuroid.app.utils.android.settings.LemuroidSettingsSlider
import com.swordfish.lemuroid.app.utils.android.settings.LemuroidSettingsSwitch
import com.swordfish.lemuroid.app.utils.android.settings.LemuroidSettingsMenuLink
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import com.swordfish.touchinput.radial.layouts.ControlEditMode

@Composable
fun GameMenuLayoutScreen(
    settings: TouchControllerSettingsManager.Settings?,
    onSettingsChanged: (TouchControllerSettingsManager.Settings) -> Unit,
    isFastForwardEnabled: Boolean,
    setFastForwardEnabled: (Boolean) -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        settings?.let { currentSettings ->
            com.alorma.compose.settings.ui.SettingsSlider(
                title = { Text(text = "Button Scale: ${String.format("%.1f", currentSettings.scale)}") },
                value = currentSettings.scale,
                valueRange = 0.2f..2.0f,
                onValueChange = { scale ->
                    onSettingsChanged(currentSettings.copy(scale = scale))
                },
                icon = {
                    androidx.compose.material3.Icon(
                        painterResource(R.drawable.ic_menu_controls),
                        contentDescription = null
                    )
                }
            )

            com.alorma.compose.settings.ui.SettingsSlider(
                title = { Text(text = "Opacity: ${(currentSettings.opacity * 100).toInt()}%") },
                value = currentSettings.opacity,
                valueRange = 0.1f..1.0f,
                onValueChange = { opacity ->
                    // LIVE PREVIEW: Bypass persistence for immediate feedback
                    com.swordfish.touchinput.radial.settings.SharedOpacityState.opacityPreview.value = opacity
                    onSettingsChanged(currentSettings.copy(opacity = opacity))
                },
                icon = {
                    androidx.compose.material3.Icon(
                        painterResource(R.drawable.ic_menu_controls),
                        contentDescription = null
                    )
                }
            )
        }

        LemuroidSettingsSwitch(
            title = { Text(text = "Fast Forward Toggle") },
            icon = {
                 androidx.compose.material3.Icon(
                    painterResource(R.drawable.ic_menu_fast_forward),
                    contentDescription = null
                )
            },
            state = com.alorma.compose.settings.storage.memory.rememberMemoryBooleanSettingState(isFastForwardEnabled),
            onCheckedChange = { setFastForwardEnabled(it) }
        )

        LemuroidSettingsMenuLink(
            title = { Text(text = "Edit Controls") },
            subtitle = { Text(text = "Drag to move, pinch to resize") },
            icon = {
                 androidx.compose.material3.Icon(
                    painterResource(R.drawable.ic_menu_controls),
                    contentDescription = null
                )
            },
            onClick = {
                ControlEditMode.isEditMode = true
                setFastForwardEnabled(isFastForwardEnabled)  // Trigger menu close
            }
        )
    }
}
