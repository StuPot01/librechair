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

package ch.deletescape.lawnchair.feed

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import ch.deletescape.lawnchair.*
import ch.deletescape.lawnchair.smartspace.LawnchairSmartspaceController.*
import ch.deletescape.lawnchair.util.Temperature
import ch.deletescape.lawnchair.util.extensions.d
import com.android.launcher3.R
import net.aksingh.owmjapis.api.APIException
import net.aksingh.owmjapis.core.OWM
import net.aksingh.owmjapis.model.DailyWeatherForecast
import net.aksingh.owmjapis.model.HourlyWeatherForecast
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class FeedWeatherStatsProvider(c: Context) : FeedProvider(c), Listener {

    private var weatherData: WeatherData? = null
    private var forecastHigh: Int? = null
    private var forecastLow: Int? = null
    private var hourlyWeatherForecast: HourlyWeatherForecast? = null
    private var dailyForecast: DailyWeatherForecast? = null
    @StringRes private var weatherTypeResource: Int? = null

    private val refreshExecutor = Executors.newSingleThreadExecutor()

    init {
        c.applicationContext.lawnchairApp.smartspace.addListener(this)
    }

    override fun onFeedShown() {
        // TODO
    }

    override fun onFeedHidden() {
        // TODO
    }

    override fun onCreate() {
        // TODO
    }

    override fun onDestroy() {
        // TODO
    }

    override fun getCards(): List<Card> {
        Log.d(javaClass.name, "getCards: " + weatherData!!)
        return if (weatherData != null && forecastHigh != null && forecastLow != null && weatherTypeResource != null) listOf(
            Card(BitmapDrawable(context.resources, weatherData!!.icon),
                 context.getString(R.string.title_card_weather_temperature,
                                   weatherData!!.getTitle(context.lawnchairPrefs.weatherUnit)),
                 object : Card.Companion.InflateHelper {
                     @SuppressLint("SetTextI18n") override fun inflate(parent: ViewGroup): View {
                         val v = LayoutInflater.from(parent.getContext())
                                 .inflate(R.layout.weather_heads_up, parent, false)
                         val highLow = v.findViewById(R.id.weather_hud_day_night) as TextView
                         val information = v.findViewById(R.id.weather_hud_information) as TextView
                         highLow.text =
                                 "${forecastHigh}${context.lawnchairPrefs.weatherUnit.suffix} / ${forecastLow}${context.lawnchairPrefs.weatherUnit.suffix}"
                         information.text = context.getString(weatherTypeResource!!)
                         if (useWhiteText(backgroundColor)) {
                             highLow.setTextColor(
                                 context.resources.getColor(R.color.textColorPrimary))
                             information.setTextColor(
                                 context.resources.getColor(R.color.textColorPrimary))
                         }
                         return v;
                     }
                 }, Card.NO_HEADER, "nosort,top"))
        else emptyList()
    }

    override fun onDataUpdated(data: DataContainer) {
        d("onDataUpdated: debug trace: ", Throwable())
        weatherData = data.weather;
        if (data.weather?.coordLat != null && data.weather.coordLong != null) {
            refreshExecutor.submit {
                val api = OWM(context.lawnchairPrefs.weatherApiKey)
                api.unit = when (context.lawnchairPrefs.weatherUnit) {
                    Temperature.Unit.Celsius -> OWM.Unit.METRIC
                    Temperature.Unit.Fahrenheit -> OWM.Unit.IMPERIAL
                    Temperature.Unit.Kelvin -> OWM.Unit.STANDARD
                    Temperature.Unit.Rakine -> TODO()
                    Temperature.Unit.Delisle -> TODO()
                    Temperature.Unit.Newton -> TODO()
                    Temperature.Unit.Reaumur -> TODO()
                    Temperature.Unit.Romer -> TODO()
                }
                d("onDataUpdated: updating forcast HUD")

                try {
                    d("onDataUpdated: fetching weather data")
                    hourlyWeatherForecast = api.hourlyWeatherForecastByCoords(data.weather.coordLat,
                                                                              data.weather.coordLong)
                    d("onDataUpdated: data retrieved")

                    val tempList: List<Int?> = hourlyWeatherForecast!!.dataList!!.map {
                        if (it!!.dateTime!!.before(tomorrow())) {
                            it.mainData?.temp?.roundToInt() as Int
                        } else {
                            null
                        }
                    }

                    d("onDataUpdated: temp list: $tempList")

                    val today = ArrayList<Int>()
                    tempList.forEach {
                        if (it != null) {
                            today.add(it)
                        }
                    }

                    d("onDataUpdated: today's weather: $today")

                    forecastLow = Collections.min(today)
                    forecastHigh = Collections.max(today)

                    d("onDataUpdated: hi: $forecastHigh lo: $forecastLow")

                    var thunder = 0
                    var rain = 0
                    var snow = 0
                    var clouds = 0
                    var clear = 0

                    d("onDataUpdated: classifying weather")

                    hourlyWeatherForecast?.dataList?.forEach {
                        if (it?.weatherList != null) {
                            it.weatherList!!.forEach {
                                val condId = it!!.conditionId!!
                                when {
                                    condId in 200..299 -> {
                                        thunder += if (condId - 200 < 10) 1 else if (condId - 200 < 20) 5 else 10
                                        rain += 5
                                    }
                                    condId in 300..399 -> {
                                        rain += 1
                                    }
                                    condId in 500..599 -> {
                                        rain += if (condId - 400 < 10) 3 else if (condId - 400 < 20) 5 else 10
                                    }
                                    condId in 600..699 -> {
                                        snow += if (condId - 600 < 10) 3 else if (condId - 600 < 20) 5 else 10
                                    }
                                    condId in 800..899 -> {
                                        if (condId != 800) {
                                            clouds += condId - 800
                                        } else {
                                            ++clear
                                        }
                                    }
                                    else -> {
                                    }
                                }
                            }
                        }
                    }

                    d("onDataUpdated: sending classification statsitics to statistics function")

                    val type = WeatherTypes
                            .getWeatherTypeFromStatistics(clear, clouds, rain, snow, thunder)

                    d("onDataUpdated: weather type is ${type.name}")

                    weatherTypeResource = when (type) {
                        WeatherTypes.CLEAR -> R.string.hud_weather_information_sunny
                        WeatherTypes.CLEAR_CLOUDS -> R.string.hud_weather_information_sunny_occasional_clouds
                        WeatherTypes.CLEAR_RAIN -> R.string.hud_weather_information_sunny_occasional_rain
                        WeatherTypes.CLEAR_SNOW -> R.string.hud_weather_information_sunny_occasional_snow
                        WeatherTypes.CLEAR_THUNDER -> R.string.hud_weather_information_sunny_occasional_thunder
                        WeatherTypes.CLOUDS -> R.string.hud_weather_information_cloudy
                        WeatherTypes.CLOUDS_CLEAR -> R.string.hud_weather_information_cloudy_occasional_sun
                        WeatherTypes.CLOUDS_RAIN -> R.string.hud_weather_information_cloudy_occasional_rain
                        WeatherTypes.CLOUDS_SNOW -> R.string.hud_weather_information_cloudy_occasional_snow
                        WeatherTypes.CLOUDS_THUNDER -> R.string.hud_weather_information_cloudy_occasional_thunder
                        WeatherTypes.RAIN -> R.string.hud_weather_information_rainy
                        WeatherTypes.RAIN_CLEAR -> R.string.hud_weather_information_rainy_occasional_sun
                        WeatherTypes.RAIN_CLOUDS -> R.string.hud_weather_information_rainy_occasional_clouds
                        WeatherTypes.RAIN_SNOW -> R.string.hud_weather_information_rainy_occasional_snow
                        WeatherTypes.RAIN_THUNDER -> R.string.hud_weather_information_rainy_occasional_thunder
                        WeatherTypes.THUNDER -> R.string.hud_weather_information_thunder
                        WeatherTypes.THUNDER_CLEAR -> R.string.hud_weather_information_thunder_occasional_sun
                        WeatherTypes.THUNDER_CLOUDS -> R.string.hud_weather_information_thunder_occasional_clouds
                        WeatherTypes.THUNDER_SNOW -> R.string.hud_weather_information_thunder_occasional_snow
                        WeatherTypes.THUNDER_RAIN -> R.string.hud_weather_information_thunder_occasional_rain
                        WeatherTypes.SNOW -> R.string.hud_weather_information_snowy
                        WeatherTypes.SNOW_CLEAR -> R.string.hud_weather_information_snowy_occasional_sun
                        WeatherTypes.SNOW_CLOUDS -> R.string.hud_weather_information_snowy_occasional_clouds
                        WeatherTypes.SNOW_RAIN -> R.string.hud_weather_information_snowy_occasional_rain
                        WeatherTypes.SNOW_THUNDER -> R.string.hud_weather_information_snowy_occasional_thunder
                    }

                    d("onDataUpdated: weather type is ${context.getString(weatherTypeResource!!)}")

                } catch (e: APIException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace();
                }
            }
        }
    }
}