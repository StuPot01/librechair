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

package ch.deletescape.lawnchair.feed;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import ch.deletescape.lawnchair.feed.impl.LauncherFeed;
import ch.deletescape.lawnchair.theme.ThemeOverride;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.functions.Function1;

public abstract class FeedProvider {

    private Context context;
    private Map<String, String> arguments;
    private FeedAdapter adapter;
    private LauncherFeed feed;
    private boolean requestedRefresh;
    private FeedProviderContainer container;
    private WindowManager windowService;

    public FeedProvider(Context c) {
        this(c, new HashMap<>());
    }

    public FeedProvider(Context c, Map<String, String> arguments) {
        this.context = c;
        this.arguments = arguments;
        this.windowService = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
    }

    public Context getContext() {
        return context;
    }


    public abstract void onFeedShown();


    public abstract void onFeedHidden();


    public abstract void onCreate();


    public abstract void onDestroy();


    public abstract List<Card> getCards();

    protected void onAttachedToAdapter(FeedAdapter adapter) {
        this.adapter = adapter;
    }

    @SuppressWarnings("WeakerAccess")
    protected void requestRefresh() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public int getBackgroundColor() {
        return adapter == null ? 0 : adapter.getBackgroundColor();
    }

    public LauncherFeed getFeed() {
        return feed;
    }

    public void setFeed(LauncherFeed feed) {
        this.feed = feed;
    }

    protected void displayView(Function1<? super ViewGroup, ? extends View> inflateHelper, float x,
            float y) {
        if (feed != null) {
            feed.displayView(inflateHelper, x, y);
        } else {
            new AlertDialog.Builder(context, new ThemeOverride.AlertDialog().getTheme(context))
                    .setView(inflateHelper.invoke(new LinearLayout(context))).show();
        }
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public FeedProviderContainer getContainer() {
        return container;
    }

    public void setContainer(FeedProviderContainer container) {
        this.container = container;
    }
}
