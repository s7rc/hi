package com.swordfish.touchinput.radial.layouts

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.util.lerp
import kotlin.math.roundToInt
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager

/**
 * Global edit mode state - set to true when user is in edit mode
 */
object ControlEditMode {
    var isEditMode by mutableStateOf(false)
    var onCustomizationChanged: ((String, TouchControllerSettingsManager.ButtonCustomization) -> Unit)? = null
}

/**
 * Wraps a control with per-button customization support
 * Applies saved offset and scale, adds gesture detection in edit mode
 */
@Composable
fun CustomizableControl(
    buttonId: String,
    settings: TouchControllerSettingsManager.Settings,
    modifier: Modifier = Modifier,
    applyGlobalScale: Boolean = true,
    content: @Composable () -> Unit
) {
    val customization = settings.perButtonCustomization[buttonId]
    
    LaunchedEffect(settings.opacity) {
        if (buttonId == "A" || buttonId == "cross") {
            android.util.Log.d("LemuroidControl", "Control $buttonId opacity: ${settings.opacity}")
        }
    }

    var localOffsetX by remember(buttonId) { mutableStateOf(customization?.offsetX ?: 0f) }
    var localOffsetY by remember(buttonId) { mutableStateOf(customization?.offsetY ?: 0f) }
    var localScale by remember(buttonId) { mutableStateOf(customization?.scale ?: 1f) }

    LaunchedEffect(customization) {
        localOffsetX = customization?.offsetX ?: 0f
        localOffsetY = customization?.offsetY ?: 0f
        localScale = customization?.scale ?: 1f
    }
    
    val globalScaleFactor = if (applyGlobalScale) {
        lerp(
            TouchControllerSettingsManager.MIN_SCALE,
            TouchControllerSettingsManager.MAX_SCALE,
            settings.scale,
        )
    } else {
        1f
    }
    
    val finalScale = if (ControlEditMode.isEditMode) {
        localScale * globalScaleFactor
    } else {
        (customization?.scale ?: 1f) * globalScaleFactor
    }

    val offsetX = if (ControlEditMode.isEditMode) localOffsetX else (customization?.offsetX ?: 0f)
    val offsetY = if (ControlEditMode.isEditMode) localOffsetY else (customization?.offsetY ?: 0f)

    val finalModifier = modifier
        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
        .scale(finalScale)
    
    androidx.compose.foundation.layout.Box(modifier = finalModifier) {
        content()
        
        if (ControlEditMode.isEditMode) {
             androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(buttonId) {
                        awaitEachGesture {
                            val down = awaitFirstDown(
                                pass = androidx.compose.ui.input.pointer.PointerEventPass.Initial,
                                requireUnconsumed = false
                            )
                            down.consume()
                            
                            var zoom = 1f
                            var pan = androidx.compose.ui.geometry.Offset.Zero
                            var dirty = false
                            
                            // Track initial position for single-finger swipe-to-scale
                            var initialSingleFingerY: Float? = null
                            var accumulatedVerticalSwipe = 0f
                            
                            do {
                                val event = awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Initial)
                                val pointerCount = event.changes.size
                                
                                if (pointerCount >= 2) {
                                    // Two+ fingers: use pinch to scale OR vertical swipe to scale
                                    val zoomChange = event.calculateZoom()
                                    val panChange = event.calculatePan()
                                    
                                    // Track average Y position for vertical swipe detection
                                    val avgY = event.changes.map { it.position.y }.average().toFloat()
                                    if (initialSingleFingerY == null) {
                                        initialSingleFingerY = avgY
                                    }
                                    
                                    val verticalDelta = avgY - (initialSingleFingerY ?: avgY)
                                    
                                    if (zoomChange != 1f || panChange != androidx.compose.ui.geometry.Offset.Zero) {
                                        localOffsetX += panChange.x
                                        localOffsetY += panChange.y
                                        
                                        // Apply pinch zoom if detected
                                        if (kotlin.math.abs(zoomChange - 1f) > 0.01f) {
                                            localScale = (localScale * zoomChange).coerceIn(0.5f, 2.5f)
                                        } 
                                        // Otherwise, check for vertical swipe to scale
                                        else if (kotlin.math.abs(verticalDelta) > 10f) {
                                            val scaleSensitivity = 0.001f
                                            val scaleChange = 1f + (verticalDelta * scaleSensitivity)
                                            localScale = (localScale * scaleChange).coerceIn(0.5f, 2.5f)
                                        }
                                        
                                        dirty = true
                                    }
                                    
                                    // Reset for next gesture
                                    accumulatedVerticalSwipe = 0f
                                } else if (pointerCount == 1) {
                                    // Single finger: move button, swipe vertically to scale
                                    val pointer = event.changes.first()
                                    val panChange = event.calculatePan()
                                    
                                    // Initialize single-finger tracking
                                    if (initialSingleFingerY == null) {
                                        initialSingleFingerY = pointer.position.y
                                    }
                                    
                                    // Calculate vertical swipe distance from initial touch
                                    val currentY = pointer.position.y
                                    val verticalDelta = currentY - (initialSingleFingerY ?: currentY)
                                    
                                    if (panChange != androidx.compose.ui.geometry.Offset.Zero) {
                                        // Always apply movement
                                        localOffsetX += panChange.x
                                        localOffsetY += panChange.y
                                        
                                        // Only apply scale if significant vertical swipe (>10px threshold)
                                        if (kotlin.math.abs(verticalDelta) > 10f) {
                                            // Every 100 pixels of vertical swipe = 0.1 scale change
                                            val scaleSensitivity = 0.001f
                                            accumulatedVerticalSwipe = verticalDelta
                                            val scaleChange = 1f + (accumulatedVerticalSwipe * scaleSensitivity)
                                            localScale = (localScale * scaleChange).coerceIn(0.5f, 2.5f)
                                        }
                                        
                                        dirty = true
                                    }
                                }
                                
                                event.changes.forEach { 
                                    it.consume()
                                }
                            } while (event.changes.any { it.pressed })

                            if (dirty) {
                                ControlEditMode.onCustomizationChanged?.invoke(
                                    buttonId,
                                    TouchControllerSettingsManager.ButtonCustomization(
                                        offsetX = localOffsetX,
                                        offsetY = localOffsetY,
                                        scale = localScale
                                    )
                                )
                            }
                        }
                    }
            )
        }
    }
}
