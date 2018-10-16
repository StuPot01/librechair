/*
 *     Copyright (c) 2017-2019 the Lawnchair team
 *     Copyright (c)  2019 oldosfan (would)
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.deletescape.lawnchair.feed.widgets

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import ch.deletescape.lawnchair.allChildren
import ch.deletescape.lawnchair.fromColorRes
import com.android.launcher3.CheckLongPressHelper
import com.android.launcher3.R
import com.android.launcher3.SimpleOnStylusPressListener
import com.android.launcher3.StylusEventHelper
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class OverlayWidgetHost(context: Context, hostId: Int) : AppWidgetHost(context, hostId) {

    override fun onCreateView(context: Context, appWidgetId: Int,
                              appWidget: AppWidgetProviderInfo): AppWidgetHostView {
        return OverlayWidgetView(context);
    }

    class OverlayWidgetView(context: Context) : AppWidgetHostView(context) {

        private val mLongPressHelper = CheckLongPressHelper(this)
        private val mStylusEventHelper = StylusEventHelper(SimpleOnStylusPressListener(this), this)

        private var scrollable = false

        var dark = false
        var shouldForceStyle = false


        private fun checkScrollableRecursively(viewGroup: ViewGroup): Boolean {
            if (viewGroup is AdapterView<*>) {
                return true
            } else {
                for (i in 0 until viewGroup.childCount) {
                    val child = viewGroup.getChildAt(i)
                    if (child is ViewGroup) {
                        if (checkScrollableRecursively(child)) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            super.onLayout(changed, left, top, right, bottom)
            scrollable = checkScrollableRecursively(this)
            if (shouldForceStyle) {
                forceStyle()
            }
        }

        override fun setAppWidget(appWidgetId: Int, info: AppWidgetProviderInfo) {
            super.setAppWidget(appWidgetId, info)
            setPadding(0, 0, 0, 0)
        }

        override fun getErrorView(): View {
            return LayoutInflater.from(this.context)
                    .inflate(R.layout.appwidget_error, this, false)
        }

        fun forceStyle() {
            allChildren.forEach { view ->
                if (dark && darkSubst.keys.any { it.isSuperclassOf(view::class) }) {
                    darkSubst.filter { it.key.isSuperclassOf(view::class) }
                            .values
                            .forEach {
                                it(view)
                            }
                } else if (lightSubst.keys.any { it.isSuperclassOf(view::class) }) {
                    lightSubst.filter { it.key.isSuperclassOf(view::class) }
                            .values
                            .forEach {
                                it(view)
                            }
                }
            }
        }

        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            if (scrollable && ev.action == MotionEvent.ACTION_DOWN) {
                parent.requestDisallowInterceptTouchEvent(true)
                parent.parent.requestDisallowInterceptTouchEvent(true)
            }
            // Just in case the previous long press hasn't been cleared, we make sure to start fresh
            // on touch down.
            if (ev.action == MotionEvent.ACTION_DOWN) {
                mLongPressHelper.cancelLongPress()
            }

            // Consume any touch events for ourselves after longpress is triggered
            if (mLongPressHelper.hasPerformedLongPress()) {
                mLongPressHelper.cancelLongPress()
                return true
            }

            // Watch for longpress or stylus button press events at this level to
            // make sure users can always pick up this widget
            if (mStylusEventHelper.onMotionEvent(ev)) {
                mLongPressHelper.cancelLongPress()
                return true
            }

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!mStylusEventHelper.inStylusButtonPressed()) {
                        mLongPressHelper.postCheckForLongPress()
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mLongPressHelper.cancelLongPress()
            }

            // Otherwise continue letting touch events fall through to children
            return super.onInterceptTouchEvent(ev)
        }

        private companion object {
            var lightSubst = mapOf(
                    (TextView::class as KClass<out View>) to { it: View ->
                        (it as TextView)
                        it.setTextColor(ColorStateList.valueOf(R.color.primary_text_material_dark
                                .fromColorRes(it.context)));
                        Unit
                    },
                    (ImageView::class as KClass<out View>) to { it: View ->
                        (it as ImageView)
                        if (it.drawable != null) {
                            it.drawable.setColorFilter(R.color.primary_text_material_dark.fromColorRes(it.context),
                                    PorterDuff.Mode.SRC)
                        }
                        Unit
                    },
                    (View::class as KClass<out View>) to { it: View ->
                        if (it.background != null) {
                            it.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC)
                        }
                    }
            )
            var darkSubst = mapOf(
                    (TextView::class as KClass<out View>) to { it: View ->
                        (it as TextView)
                        it.setTextColor(ColorStateList.valueOf(R.color.primary_text_material_light
                                .fromColorRes(it.context)));
                        Unit
                    },
                    (ImageView::class as KClass<out View>) to { it: View ->
                        (it as ImageView)
                        if (it.drawable != null) {
                            it.drawable.setColorFilter(R.color.primary_text_material_light.fromColorRes(it.context),
                                    PorterDuff.Mode.SRC)
                        }
                        Unit
                    },
                    (View::class as KClass<out View>) to { it: View ->
                        if (it.background != null) {
                            it.background.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC)
                        }
                    }
            )
        }
    }
}