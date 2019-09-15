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

package ch.deletescape.lawnchair.feed.wikipedia.image

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object DailyImage {
    val uriCache = mutableMapOf<Long, String?>()
    private val API_URL = "https://en.wikipedia.org/api/rest_v1/feed/featured/2496/04/01"
    suspend fun getFeaturedImage(): String? = uriCache[TimeUnit.MILLISECONDS.toDays(
            System.currentTimeMillis())] ?: GlobalScope.async {
         JsonParser().parse(IOUtils.toString(URL(API_URL).openStream(), Charset.defaultCharset())).asJsonObject.getAsJsonObject(
                "image")?.getAsJsonObject("image")?.getAsJsonPrimitive("source")?.asString
    }.await().also { uriCache[TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())] = it }

    suspend fun safeGetFeaturedImage(): String? {
        try {
            return getFeaturedImage()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: JsonParseException) {
            e.printStackTrace()
            return null
        }
    }
}