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
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.launcher3.R;
import com.android.launcher3.Utilities;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import ch.deletescape.lawnchair.LawnchairUtilsKt;
import io.github.cdimascio.essence.Essence;
import kotlin.Unit;

public class ArticleViewerScreen extends ProviderScreen {

    private final String title;
    private final String categories;
    private final String url;
    private final String desc;

    public ArticleViewerScreen(Context base, String title, String categories, String url,
                               String desc) {
        super(base);
        this.title = title;
        this.categories = categories;
        this.url = url;
        this.desc = desc;

        addAction(new FeedProvider.Action(getDrawable(R.drawable.ic_share),
                getString(getResources()
                        .getIdentifier("whichSendApplicationLabel", "string", "android")), () -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, url);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }));
    }

    @Override
    protected View getView(ViewGroup parent) {
        return LawnchairUtilsKt.inflate(parent, R.layout.overlay_article);
    }

    @Override
    protected void bindView(View articleView) {
        TextView titleView = articleView.findViewById(R.id.title);
        TextView contentView = articleView.findViewById(R.id.content);
        Button openInBrowser = articleView.findViewById(R.id.open_externally);
        openInBrowser.setOnClickListener(v3 -> {
            try {
                WebViewScreen screen = new WebViewScreen(this, url, webView -> {
                    webView.getSettings().setJavaScriptEnabled(false);
                });
                screen.display(this,
                        LawnchairUtilsKt.getPostionOnScreen(v3).getFirst() + v3.getMeasuredWidth() / 2,
                        LawnchairUtilsKt.getPostionOnScreen(
                                v3).getSecond() + v3.getMeasuredHeight() / 2);
            } catch (IllegalStateException e) {
                Utilities.openURLinBrowser(this, url);
            }
        });
        openInBrowser.setTextColor(FeedAdapter.Companion.getOverrideColor(articleView.getContext(),
                LawnchairUtilsKt.getColorEngineAccent(this)));
        titleView.setText(title);
        TextView categoriesView = articleView
                .findViewById(R.id.article_categories);
        categoriesView.setText(categories);
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                URLConnection urlConnection = new URL(
                        url.replace("http://", "https://"))
                        .openConnection();
                if (urlConnection instanceof HttpURLConnection) {
                    ((HttpURLConnection) urlConnection)
                            .setInstanceFollowRedirects(true);
                }
                CharSequence text = Essence
                        .extract(IOUtils.toString(urlConnection
                                .getInputStream(), Charset
                                .defaultCharset())).getText();
                if (text.toString().trim().isEmpty()) {
                    text = Html
                            .fromHtml(desc, 0);
                }
                CharSequence finalText = text;
                contentView.post(() -> contentView.setText(finalText));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
