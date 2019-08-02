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

import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import ch.deletescape.lawnchair.LawnchairApp
import ch.deletescape.lawnchair.feed.Card
import ch.deletescape.lawnchair.feed.FeedProvider
import ch.deletescape.lawnchair.lawnchairPrefs
import ch.deletescape.lawnchair.util.extensions.d
import java.util.concurrent.Executors

class FeedWidgetsProvider(c: Context) : FeedProvider(c) {
    val hostViewCache = mutableMapOf<Int, AppWidgetHostView>()
    val inflateExecutor = Executors.newFixedThreadPool(5)

    val appWidgetManager by lazy {
        context.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
    }

    override fun onFeedShown() {

    }

    override fun onFeedHidden() {

    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun getCards(): List<Card> {
        d("getCards: feed widgets list: ${context.lawnchairPrefs.feedWidgetList.getList()}")
        return context.lawnchairPrefs.feedWidgetList.getAll()
                .filter { appWidgetManager.getAppWidgetInfo(it) != null }.map {
                    Card(if (context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second?.showCardTitle != true) null else appWidgetManager.getAppWidgetInfo(
                            it).loadIcon(context, context.resources.displayMetrics.densityDpi),
                         if (context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second?.showCardTitle != true) null else appWidgetManager.getAppWidgetInfo(
                                 it).loadLabel(context.packageManager), { parent, _ ->
                             if (hostViewCache.containsKey(it)) {
                                 hostViewCache[it] ?: error("")
                             } else {
                                 (parent.context.applicationContext as LawnchairApp)
                                         .overlayWidgetHost.createView(parent.context, it,
                                                                       appWidgetManager.getAppWidgetInfo(
                                                                               it).apply {
                                                                           minWidth = parent.width
                                                                       }).apply {
                                     setExecutor(inflateExecutor)
                                     layoutParams = ViewGroup
                                             .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                           (parent.context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second
                                                            ?: WidgetMetadata.DEFAULT).height
                                                           ?: appWidgetInfo.minHeight)
                                     updateAppWidgetOptions(Bundle().apply {
                                         putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, width)
                                         putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, width)
                                         putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT,
                                                (parent.context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second
                                                 ?: WidgetMetadata.DEFAULT).height ?: height)
                                         putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT,
                                                (parent.context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second
                                                 ?: WidgetMetadata.DEFAULT).height ?: height)
                                     })
                                     invalidate()
                                 }.also { it2 ->
                                     hostViewCache += it to it2; it2.invalidate()
                                 }
                             }
                         },
                         if ((context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second
                              ?: WidgetMetadata.DEFAULT).raiseCard) if (!(context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second
                                                                          ?: WidgetMetadata.DEFAULT).showCardTitle) Card.NO_HEADER or Card.RAISE else Card.RAISE else if (!(context.lawnchairPrefs.feedWidgetMetadata.getAll().firstOrNull { it2 -> it2.first == it }?.second
                                                                                                                                                                            ?: WidgetMetadata.DEFAULT).showCardTitle) Card.NO_HEADER else Card.DEFAULT,
                         "nosort, top", it shl 2)
                }
    }
}