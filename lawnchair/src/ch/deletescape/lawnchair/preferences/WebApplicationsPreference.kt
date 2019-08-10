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

package ch.deletescape.lawnchair.preferences

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.preference.DialogPreference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import ch.deletescape.lawnchair.LawnchairPreferences
import ch.deletescape.lawnchair.feed.WebApplication
import ch.deletescape.lawnchair.fromStringRes
import ch.deletescape.lawnchair.lawnchairPrefs
import ch.deletescape.lawnchair.theme.ThemeOverride
import com.android.launcher3.R
import java.net.MalformedURLException
import java.net.URL

class WebApplicationsPreference(context: Context?, attrs: AttributeSet?) :
        DialogPreference(context, attrs), LawnchairPreferences.OnPreferenceChangeListener {
    override fun onValueChanged(key: String, prefs: LawnchairPreferences, force: Boolean) {
        // TODO refresh summary when pref changed
    }

    override fun getNegativeButtonText(): CharSequence {
        return context.getString(R.string.negative_button_test_web_applications_preference)
    }

    override fun getDialogLayoutResource() = R.layout.dialog_preference_recyclerview
    class Fragment : PreferenceDialogFragmentCompat() {
        lateinit var recyclerView: RecyclerView
        override fun onDialogClosed(positiveResult: Boolean) {
            if (!positiveResult) {
                val dialog = object :
                        AlertDialog(context, ThemeOverride.AlertDialog().getTheme(context!!)) {}
                val context = context
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.dialog_create_web_app, null, false)
                dialog.setTitle(R.string.title_dialog_new_web_app)
                dialog.setView(view)
                dialog.setButton(Dialog.BUTTON_POSITIVE,
                                 android.R.string.ok.fromStringRes(context!!)) { _, _ ->
                    val title = dialog.findViewById<TextView>(R.id.web_app_title).text
                    val link = dialog.findViewById<TextView>(R.id.web_app_link).text
                    if (title.isEmpty() || link.isEmpty()) {
                        return@setButton
                    } else {
                        try {
                            context.lawnchairPrefs.feedWebApplications += WebApplication().apply {
                                this.isShortcut = false
                                this.url = URL(link.toString())
                                this.title = title.toString()
                            }
                            recyclerView.adapter
                                    ?.notifyItemInserted(recyclerView.adapter?.itemCount ?: 0)
                        } catch (e: MalformedURLException) {
                            Toast.makeText(context, R.string.title_toast_url_invalid,
                                           Toast.LENGTH_LONG).show()
                        }
                    }
                }
                dialog.show()
            }
        }

        override fun onBindDialogView(view: View) {
            super.onBindDialogView(view)
            recyclerView = view.findViewById(R.id.list) as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = Adapter()
        }

        companion object {
            fun make() = Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_KEY, "pref_feed_web_applications")
                }
            }
        }

        inner class Adapter : RecyclerView.Adapter<ProviderItemViewHolder>() {
            override fun getItemCount(): Int {
                return context?.lawnchairPrefs?.feedWebApplications?.size ?: 0
            }

            override fun onBindViewHolder(holder: ProviderItemViewHolder, position: Int) {
                val app = context?.lawnchairPrefs?.feedWebApplications?.get(position)
                holder.title.text = app?.title
                holder.summary.text = app?.title.also {
                    if (it != null) holder.summary.visibility == View.VISIBLE else holder.summary
                            .visibility = View.GONE
                }
                holder.dragHandle.visibility = View.VISIBLE
            }

            override fun onCreateViewHolder(parent: ViewGroup,
                                            viewType: Int) = ProviderItemViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.event_provider_dialog_item,
                                                         parent, false))
        }
    }
}