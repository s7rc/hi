package com.swordfish.lemuroid.app.mobile.feature.gamemenu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Activity for per-button touch control customization
 * Allows dragging and pinch-to-resize individual buttons
 */
class TouchControlsEditActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.8f)
                ) {
                    TouchControlsEditScreen(
                        onDone = {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    )
                }
            }
        }
    }
    
    companion object {
        fun newIntent(activity: Activity): Intent {
            return Intent(activity, TouchControlsEditActivity::class.java)
        }
    }
}

@Composable
fun TouchControlsEditScreen(onDone: () -> Unit) {
    // Store per-button offsets and scales
    val buttonCustomizations = remember { mutableStateMapOf<String, ButtonState>() }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Instructions
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EDIT MODE\nDrag buttons to move\nPinch to resize\nPress BACK when done",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        // Example draggable/resizable buttons
        // In real implementation, these would be the actual touch control buttons
        EditableButton(
            buttonId = "button_a",
            initialX = 100f,
            initialY = 100f,
            initialScale = 1f,
            customizations = buttonCustomizations
        ) {
            Text("A", color = Color.White)
        }
        
        EditableButton(
            buttonId = "button_b",
            initialX = 200f,
            initialY = 100f,
            initialScale = 1f,
            customizations = buttonCustomizations
        ) {
            Text("B", color = Color.White)
        }
    }
}

data class ButtonState(
    val offsetX: Float,
    val offsetY: Float,
    val scale: Float
)

@Composable
fun EditableButton(
    buttonId: String,
    initialX: Float,
    initialY: Float,
    initialScale: Float,
    customizations: MutableMap<String, ButtonState>,
    content: @Composable BoxScope.() -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(initialX) }
    var offsetY by remember { mutableFloatStateOf(initialY) }
    var scale by remember { mutableFloatStateOf(initialScale) }
    
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size((80 * scale).dp)
            .pointerInput(Unit) {
                // Detect drag gestures
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    
                    // Save to customizations map
                    customizations[buttonId] = ButtonState(offsetX, offsetY, scale)
                }
            }
            .pointerInput(Unit) {
                // Detect pinch gestures for resize
                detectTransformGestures { _, _, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 2.5f)
                    
                    // Save to customizations map
                    customizations[buttonId] = ButtonState(offsetX, offsetY, scale)
                }
            }
            .background(Color.Red.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
