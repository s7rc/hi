package com.swordfish.touchinput.radial.utils

import androidx.compose.ui.geometry.Offset
import com.swordfish.lemuroid.common.graphics.GraphicsUtils.rotatePoint
import com.swordfish.lemuroid.common.math.computeSizeOfItemsAroundCircumference
import com.swordfish.touchinput.radial.settings.TouchControllerSettingsManager
import gg.padkit.anchors.Anchor
import gg.padkit.ids.Id
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun buildCentral6ButtonsAnchors(
    rotation: Float,
    id0: Int,
    id1: Int,
    id2: Int,
    id3: Int,
): PersistentList<Anchor<Id.Key>> {
    val buttonSize = computeSizeOfItemsAroundCircumference(12)
    val distance = 3f * buttonSize

    val rotationAngle = rotation * TouchControllerSettingsManager.MAX_ROTATION.toDouble()

    val delta = Offset(-tan(Math.toRadians(15.0)).toFloat(), 1f) * distance * 1.25f
    val topLeftLine = Offset(0f, -1f - distance)
    val topRightLine = Offset(sin(Math.toRadians(30.0)).toFloat(), -cos(Math.toRadians(30.0)).toFloat())

    val pointA = rotatePoint(topLeftLine + delta * 1.0f, rotationAngle)
    val pointB = rotatePoint(topLeftLine + delta * 2.0f, rotationAngle)
    val pointC = rotatePoint(topRightLine * (1f + distance) + delta * 1.0f, rotationAngle)
    val pointD = rotatePoint(topRightLine * (1f + distance) + delta * 2.0f, rotationAngle)

    val result =
        persistentListOf(
            Anchor(pointA, persistentSetOf(Id.Key(id0)), buttonSize),
            Anchor(pointB, persistentSetOf(Id.Key(id1)), buttonSize),
            Anchor(pointC, persistentSetOf(Id.Key(id2)), buttonSize),
            Anchor(pointD, persistentSetOf(Id.Key(id3)), buttonSize),
        )

    return result
}

fun buildSega6ButtonsAnchors(
    rotation: Float,
    idTopLeft: Int,
    idTopCenter: Int,
    idTopRight: Int,
    idBottomLeft: Int,
    idBottomCenter: Int,
    idBottomRight: Int,
): PersistentList<Anchor<Id.Key>> {
    val buttonSize = computeSizeOfItemsAroundCircumference(12)
    val spacing = buttonSize * 2.8f // Adjusted spacing to 2.8f (User: 3.5 too far, 2.2 too close)
    val rotationAngle = rotation * TouchControllerSettingsManager.MAX_ROTATION.toDouble()

    // Define relative positions for a 3x2 grid (centered)
    // Row 1 (Top): Y = -spacing/2
    // Row 2 (Bottom): Y = spacing/2
    // Cols: Left=-spacing, Center=0, Right=spacing
    val rowOffset = 0.6f * spacing // Tilt slightly? No, keep simple grid first.
    
    // Slight arc?
    // Let's do straight grid for now, shifted slightly up to clear thumb.
    // Actually, create 6 explicit points normally around the center.
    
    // Top Row (X Y Z)
    val pTopLeft = Offset(-spacing, -rowOffset)
    val pTopCenter = Offset(0f, -rowOffset)
    val pTopRight = Offset(spacing, -rowOffset)

    // Bottom Row (A B C)
    val pBottomLeft = Offset(-spacing, rowOffset)
    val pBottomCenter = Offset(0f, rowOffset)
    val pBottomRight = Offset(spacing, rowOffset)

    // Rotate all
    val aTopLeft = rotatePoint(pTopLeft, rotationAngle)
    val aTopCenter = rotatePoint(pTopCenter, rotationAngle)
    val aTopRight = rotatePoint(pTopRight, rotationAngle)
    val aBottomLeft = rotatePoint(pBottomLeft, rotationAngle)
    val aBottomCenter = rotatePoint(pBottomCenter, rotationAngle)
    val aBottomRight = rotatePoint(pBottomRight, rotationAngle)

    return persistentListOf(
        Anchor(aTopLeft, persistentSetOf(Id.Key(idTopLeft)), buttonSize),
        Anchor(aTopCenter, persistentSetOf(Id.Key(idTopCenter)), buttonSize),
        Anchor(aTopRight, persistentSetOf(Id.Key(idTopRight)), buttonSize),
        Anchor(aBottomLeft, persistentSetOf(Id.Key(idBottomLeft)), buttonSize),
        Anchor(aBottomCenter, persistentSetOf(Id.Key(idBottomCenter)), buttonSize),
        Anchor(aBottomRight, persistentSetOf(Id.Key(idBottomRight)), buttonSize),
    )
}
