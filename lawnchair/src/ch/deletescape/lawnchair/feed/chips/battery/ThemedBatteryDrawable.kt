/*
 * Copyright (c) 2019 oldosfan.
 * Copyright (c) 2019 the Lawnchair developers
 *
 *     This file is part of Librechair.
 *
 *     Librechair is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Librechair is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Librechair.  If not, see <https://www.gnu.org/licenses/>.
 */
package ch.deletescape.lawnchair.feed.chips.battery

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.PathParser
import android.util.TypedValue
import com.android.launcher3.R
import com.android.launcher3.Utilities

/**
 * A battery meter drawable that respects paths configured in
 * frameworks/base/core/res/res/values/config.xml to allow for an easily overrideable battery icon
 */
open class ThemedBatteryDrawable(private val context: Context, frameColor: Int) : Drawable() {
    // Need to load:
    // 1. perimeter shape
    // 2. fill mask (if smaller than perimeter, this would create a fill that
    //    doesn't touch the walls
    private val perimeterPath = Path()
    private val scaledPerimeter = Path()
    private val errorPerimeterPath = Path()
    private val scaledErrorPerimeter = Path()
    // Fill will cover the whole bounding rect of the fillMask, and be masked by the path
    private val fillMask = Path()
    private val scaledFill = Path()
    // Based off of the mask, the fill will interpolate across this space
    private val fillRect = RectF()
    // Top of this rect changes based on level, 100% == fillRect
    private val levelRect = RectF()
    private val levelPath = Path()
    // Updates the transform of the paths when our bounds change
    private val scaleMatrix = Matrix()
    private val padding = Rect()
    // The net result of fill + perimeter paths
    private val unifiedPath = Path()
    // Bolt path (used while charging)
    private val boltPath = Path()
    private val scaledBolt = Path()
    // Plus sign (used for power save mode)
    private val plusPath = Path()
    private val scaledPlus = Path()
    private var intrinsicHeight: Int
    private var intrinsicWidth: Int
    // To implement hysteresis, keep track of the need to invert the interior icon of the battery
    private var invertFillIcon = false
    // Colors can be configured based on battery level (see res/values/arrays.xml)
    private var colorLevels: IntArray
    private var fillColor: Int = Color.MAGENTA
    private var backgroundColor: Int = Color.MAGENTA
    // updated whenever level changes
    private var levelColor: Int = Color.MAGENTA
    // Dual tone implies that battery level is a clipped overlay over top of the whole shape
    private var dualTone = false
    private var batteryLevel = 0
    private val invalidateRunnable: () -> Unit = {
        invalidateSelf()
    }
    open var criticalLevel: Int = context.resources.getInteger(
            com.android.internal.R.integer.config_criticalBatteryWarningLevel)
    var charging = false
        set(value) {
            field = value
            postInvalidate()
        }
    var powerSaveEnabled = false
        set(value) {
            field = value
            postInvalidate()
        }
    private val fillColorStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).also { p ->
        p.color = frameColor
        p.isDither = true
        p.strokeWidth = 5f
        p.style = Paint.Style.STROKE
        if (Utilities.ATLEAST_Q) {
            p.blendMode = BlendMode.SRC
        }
        p.strokeMiter = 5f
        p.strokeJoin = Paint.Join.ROUND
    }
    private val fillColorStrokeProtection = Paint(Paint.ANTI_ALIAS_FLAG).also { p ->
        p.isDither = true
        p.strokeWidth = 5f
        p.style = Paint.Style.STROKE
        if (Utilities.ATLEAST_Q) {
            p.blendMode = BlendMode.CLEAR
        }
        p.strokeMiter = 5f
        p.strokeJoin = Paint.Join.ROUND
    }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).also { p ->
        p.color = frameColor
        p.alpha = 255
        p.isDither = true
        p.strokeWidth = 0f
        p.style = Paint.Style.FILL_AND_STROKE
    }
    private val errorPaint = Paint(Paint.ANTI_ALIAS_FLAG).also { p ->
        p.color = Color.RED
        p.alpha = 255
        p.isDither = true
        p.strokeWidth = 0f
        p.style = Paint.Style.FILL_AND_STROKE
        if (Utilities.ATLEAST_Q) {
            p.blendMode = BlendMode.SRC
        }
    }
    // Only used if dualTone is set to true
    private val dualToneBackgroundFill = Paint(Paint.ANTI_ALIAS_FLAG).also { p ->
        p.color = frameColor
        p.alpha = 255
        p.isDither = true
        p.strokeWidth = 0f
        p.style = Paint.Style.FILL_AND_STROKE
    }

    init {
        val density = context.resources.displayMetrics.density
        intrinsicHeight = (Companion.HEIGHT * density).toInt()
        intrinsicWidth = (Companion.WIDTH * density).toInt()
        val res = context.resources
        val levels = res.obtainTypedArray(R.array.batterymeter_color_levels)
        val colors = res.obtainTypedArray(R.array.batterymeter_color_values)
        val N = levels.length()
        colorLevels = IntArray(2 * N)
        for (i in 0 until N) {
            colorLevels[2 * i] = levels.getInt(i, 0)
            if (colors.getType(i) == TypedValue.TYPE_ATTRIBUTE) {
                colorLevels[2 * i + 1] = Utilities.getColorAttrDefaultColor(context,
                        colors.getThemeAttributeId(i, 0))
            } else {
                colorLevels[2 * i + 1] = colors.getColor(i, 0)
            }
        }
        levels.recycle()
        colors.recycle()
        loadPaths()
    }

    override fun draw(c: Canvas) {
        c.saveLayer(null, null)
        unifiedPath.reset()
        levelPath.reset()
        levelRect.set(fillRect)
        val fillFraction = batteryLevel / 100f
        val fillTop =
                if (batteryLevel >= 95)
                    fillRect.top
                else
                    fillRect.top + (fillRect.height() * (1 - fillFraction))
        levelRect.top = Math.floor(fillTop.toDouble()).toFloat()
        levelPath.addRect(levelRect, Path.Direction.CCW)
        // The perimeter should never change
        unifiedPath.addPath(scaledPerimeter)
        // If drawing dual tone, the level is used only to clip the whole drawable path
        if (!dualTone) {
            unifiedPath.op(levelPath, Path.Op.UNION)
        }
        fillPaint.color = levelColor
        // Deal with unifiedPath clipping before it draws
        if (charging) {
            // Clip out the bolt shape
            unifiedPath.op(scaledBolt, Path.Op.DIFFERENCE)
            if (!invertFillIcon) {
                c.drawPath(scaledBolt, fillPaint)
            }
        }
        if (dualTone) {
            // Dual tone means we draw the shape again, clipped to the charge level
            c.drawPath(unifiedPath, dualToneBackgroundFill)
            c.save()
            c.clipRect(0f,
                    bounds.bottom - bounds.height() * fillFraction,
                    bounds.right.toFloat(),
                    bounds.bottom.toFloat())
            c.drawPath(unifiedPath, fillPaint)
            c.restore()
        } else {
            // Non dual-tone means we draw the perimeter (with the level fill), and potentially
            // draw the fill again with a critical color
            fillPaint.color = fillColor
            c.drawPath(unifiedPath, fillPaint)
            fillPaint.color = levelColor
            // Show colorError below this level
            if (batteryLevel <= Companion.CRITICAL_LEVEL && !charging) {
                c.save()
                c.clipPath(scaledFill)
                c.drawPath(levelPath, fillPaint)
                c.restore()
            }
        }
        if (charging) {
            c.clipOutPath(scaledBolt)
            if (invertFillIcon) {
                c.drawPath(scaledBolt, fillColorStrokePaint)
            } else {
                c.drawPath(scaledBolt, fillColorStrokeProtection)
            }
        } else if (powerSaveEnabled) {
            // If power save is enabled draw the perimeter path with colorError
            c.drawPath(scaledErrorPerimeter, errorPaint)
            // And draw the plus sign on top of the fill
            c.drawPath(scaledPlus, errorPaint)
        }
        c.restore()
    }

    private fun batteryColorForLevel(level: Int): Int {
        return when {
            charging || powerSaveEnabled -> fillColor
            else -> getColorForLevel(level)
        }
    }

    private fun getColorForLevel(level: Int): Int {
        var thresh: Int
        var color = 0
        var i = 0
        while (i < colorLevels.size) {
            thresh = colorLevels[i]
            color = colorLevels[i + 1]
            if (level <= thresh) {
                // Respect tinting for "normal" level
                return if (i == colorLevels.size - 2) {
                    fillColor
                } else {
                    color
                }
            }
            i += 2
        }
        return color
    }

    /**
     * Alpha is unused internally, and should be defined in the colors passed to {@link setColors}.
     * Further, setting an alpha for a dual tone battery meter doesn't make sense without bounds
     * defining the minimum background fill alpha. This is because fill + background must be equal
     * to the net alpha passed in here.
     */
    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        fillPaint.colorFilter = colorFilter
        fillColorStrokePaint.colorFilter = colorFilter
        dualToneBackgroundFill.colorFilter = colorFilter
    }

    /**
     * Deprecated, but required by Drawable
     */
    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun getIntrinsicHeight(): Int {
        return intrinsicHeight
    }

    override fun getIntrinsicWidth(): Int {
        return intrinsicWidth
    }

    /**
     * Set the fill level
     */
    public open fun setBatteryLevel(l: Int) {
        invertFillIcon = if (l >= 67) true else if (l <= 33) false else invertFillIcon
        batteryLevel = l
        levelColor = batteryColorForLevel(batteryLevel)
        invalidateSelf()
    }

    public fun getBatteryLevel(): Int {
        return batteryLevel
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        updateSize()
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        padding.left = left
        padding.top = top
        padding.right = right
        padding.bottom = bottom
        updateSize()
    }

    fun setColors(fgColor: Int, bgColor: Int, singleToneColor: Int) {
        fillColor = if (dualTone) fgColor else singleToneColor
        fillPaint.color = fillColor
        fillColorStrokePaint.color = fillColor
        backgroundColor = bgColor
        dualToneBackgroundFill.color = bgColor
        // Also update the level color, since fillColor may have changed
        levelColor = batteryColorForLevel(batteryLevel)
        invalidateSelf()
    }

    private fun postInvalidate() {
        unscheduleSelf(invalidateRunnable)
        scheduleSelf(invalidateRunnable, 0)
    }

    private fun updateSize() {
        val b = bounds
        if (b.isEmpty) {
            scaleMatrix.setScale(1f, 1f)
        } else {
            scaleMatrix.setScale((b.right / WIDTH), (b.bottom / HEIGHT))
        }
        perimeterPath.transform(scaleMatrix, scaledPerimeter)
        errorPerimeterPath.transform(scaleMatrix, scaledErrorPerimeter)
        fillMask.transform(scaleMatrix, scaledFill)
        scaledFill.computeBounds(fillRect, true)
        boltPath.transform(scaleMatrix, scaledBolt)
        plusPath.transform(scaleMatrix, scaledPlus)
        // It is expected that this view only ever scale by the same factor in each dimension, so
        // just pick one to scale the strokeWidths
        val scaledStrokeWidth =
                Math.max(b.right / WIDTH * PROTECTION_STROKE_WIDTH, PROTECTION_MIN_STROKE_WIDTH)
        fillColorStrokePaint.strokeWidth = scaledStrokeWidth
        fillColorStrokeProtection.strokeWidth = scaledStrokeWidth
    }

    private fun loadPaths() {
        val pathString = if (Utilities.ATLEAST_Q) context.resources.getString(
                com.android.internal.R.string.config_batterymeterPerimeterPath) else
            "M3.5,2 v0 H1.33 C0.6,2 0,2.6 0,3.33 V13v5.67 C0,19.4 0.6,20 1.33,20 " +
                    "h9.33 C11.4,20 12,19.4 12,18.67 V13V3.33 C12,2.6 11.4,2 10.67,2" +
                    " H8.5 V0 H3.5 z M2,18v-7V4h8v9v5H2L2,18z\n"
        perimeterPath.set(PathParser.createPathFromPathData(pathString))
        perimeterPath.computeBounds(RectF(), true)
        val errorPathString = if (Utilities.ATLEAST_Q) context.resources.getString(
                com.android.internal.R.string.config_batterymeterErrorPerimeterPath) else pathString
        errorPerimeterPath.set(PathParser.createPathFromPathData(errorPathString))
        errorPerimeterPath.computeBounds(RectF(), true)
        val fillMaskString = if (Utilities.ATLEAST_Q) context.resources.getString(
                com.android.internal.R.string.config_batterymeterFillMask) else "M2,18 v-14 h8 v14 z"
        fillMask.set(PathParser.createPathFromPathData(fillMaskString))
        // Set the fill rect so we can calculate the fill properly
        fillMask.computeBounds(fillRect, true)
        val boltPathString = if (Utilities.ATLEAST_Q) context.resources.getString(
                com.android.internal.R.string.config_batterymeterBoltPath) else "M5,17.5 V12 H3 L7,4.5 V10 h2 L5,17.5 z"
        boltPath.set(PathParser.createPathFromPathData(boltPathString))
        val plusPathString = if (Utilities.ATLEAST_Q) context.resources.getString(
                com.android.internal.R.string.config_batterymeterPowersavePath) else "M9,10l-2,0l0,-2l-2,0l0,2l-2,0l0,2l2,0l0,2l2,0l0,-2l2,0z"
        plusPath.set(PathParser.createPathFromPathData(plusPathString))
        dualTone = if (Utilities.ATLEAST_Q) context.resources.getBoolean(
                com.android.internal.R.bool.config_batterymeterDualTone) else false
    }

    companion object {
        private const val TAG = "ThemedBatteryDrawable"
        private const val WIDTH = 12f
        private const val HEIGHT = 20f
        private const val CRITICAL_LEVEL = 15
        // On a 12x20 grid, how wide to make the fill protection stroke.
        // Scales when our size changes
        private const val PROTECTION_STROKE_WIDTH = 3f
        // Arbitrarily chosen for visibility at small sizes
        private const val PROTECTION_MIN_STROKE_WIDTH = 6f
    }
}