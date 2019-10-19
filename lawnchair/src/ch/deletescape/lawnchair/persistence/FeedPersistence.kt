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

package ch.deletescape.lawnchair.persistence

import android.content.Context
import ch.deletescape.lawnchair.feed.maps.MapProvider
import ch.deletescape.lawnchair.util.SingletonHolder
import com.android.launcher3.BuildConfig

class FeedPersistence private constructor(val context: Context) {
    val useBackgroundImageAsScreenBackground
            by BooleanDelegate(context, "feed_background_as_screen_background", true)
    val useBoxBackgroundBlur
            by BooleanDelegate(context, "feed_boxed_background_blur", false)
    val useJavascriptInSearchScreen
            by BooleanDelegate(context, "feed_javascript_in_search_screen", false)
    val displayActionsAsMenu
            by BooleanDelegate(context, "feed_actions_as_menu", false)
    val mapProvider by DefValueStringDelegate(context, "feed_map_provider",
            MapProvider::class.qualifiedName!!)
    val enableHostsFilteringInWebView
            by BooleanDelegate(context, "feed_hosts_filtering", true)
    val directlyOpenLinksInBrowser
            by BooleanDelegate(context, "feed_directly_open_links", false)
    val conservativeRefreshes
            by BooleanDelegate(context, "feed_conservative_refreshes", true)
    val pullDownToRefresh
            by BooleanDelegate(context, "feed_pull_down_to_refresh", true)
    val useGecko
            by BooleanDelegate(context, "feed_gecko", BuildConfig.GECKO)
    val notifyUsersAboutNewArticlesOnFirstRun
            by BooleanDelegate(context, "feed_notify_articles_on_first_run", false)
    val notificationCount
            by NumberDelegate(context, "feed_synd_notification_count", 5.0)
    val toolbarOpacity
            by NumberDelegate(context, "feed_toolbar_opacity", 0.5)
    val useRSSMinicard
            by BooleanDelegate(context, "feed_rss_minicard", false)
    val hideActions
            by BooleanDelegate(context, "feed_hide_toolbar_actions", false)
    val flatCardVerticalPadding
            by DipDimenDelegate(context, "feed_flat_card_vertical_margin", 16.0)
    val flatCardHorizontalPadding
            by DipDimenDelegate(context, "feed_flat_card_horizontal_margin", 16.0)
    val hideToolbar
            by BooleanDelegate(context, "feed_hide_title_bar", false)
    val cardCornerRadius
            by DipDimenDelegate(context, "feed_card_corner_radius", 0.0)
    val openingAnimationSpeed
            by NumberDelegate(context, "feed_opening_animation_speed", 0.5)

    companion object : SingletonHolder<FeedPersistence, Context>(::FeedPersistence)
}

val Context.feedPrefs get() = FeedPersistence.getInstance(this)