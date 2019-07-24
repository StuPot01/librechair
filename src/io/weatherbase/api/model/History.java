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

/*
 * Weatherbit.io - Swagger UI Weather API documentation
 * This is the documentation for the Weatherbit Weather API.  The base URL for the API is [http://api.weatherbit.io/v2.0/](http://api.weatherbit.io/v2.0/) or [https://api.weatherbit.io/v2.0/](http://api.weatherbit.io/v2.0/). Below is the Swagger UI documentation for the API. All API requests require the `key` parameter.        An Example for a 5 day forecast for London, UK would be `http://api.weatherbit.io/v2.0/forecast/3hourly?city=London`&`country=UK`. See our [Weather API description page](https://www.weatherbit.io/api) for additional documentation.
 *
 * OpenAPI spec version: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.weatherbase.api.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * History
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-07-23T18:38:21.490044+08:00[Asia/Shanghai]")
public class History {
  @SerializedName("city_name")
  private String cityName = null;

  @SerializedName("state_code")
  private String stateCode = null;

  @SerializedName("country_code")
  private String countryCode = null;

  @SerializedName("timezone")
  private String timezone = null;

  @SerializedName("lat")
  private String lat = null;

  @SerializedName("lon")
  private String lon = null;

  @SerializedName("sources")
  private List<String> sources = null;

  @SerializedName("data")
  private List<HistoryObj> data = null;

  public History cityName(String cityName) {
    this.cityName = cityName;
    return this;
  }

   /**
   * City name (Closest)
   * @return cityName
  **/
  @Schema(example = "Seattle", description = "City name (Closest)")
  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public History stateCode(String stateCode) {
    this.stateCode = stateCode;
    return this;
  }

   /**
   * State abbreviation
   * @return stateCode
  **/
  @Schema(example = "WA", description = "State abbreviation")
  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public History countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

   /**
   * Country abbreviation
   * @return countryCode
  **/
  @Schema(example = "US", description = "Country abbreviation")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public History timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

   /**
   * Local IANA time zone
   * @return timezone
  **/
  @Schema(example = "America/New_York", description = "Local IANA time zone")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public History lat(String lat) {
    this.lat = lat;
    return this;
  }

   /**
   * Latitude
   * @return lat
  **/
  @Schema(example = "47.61", description = "Latitude")
  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public History lon(String lon) {
    this.lon = lon;
    return this;
  }

   /**
   * Longitude
   * @return lon
  **/
  @Schema(example = "-122.33", description = "Longitude")
  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public History sources(List<String> sources) {
    this.sources = sources;
    return this;
  }

  public History addSourcesItem(String sourcesItem) {
    if (this.sources == null) {
      this.sources = new ArrayList<String>();
    }
    this.sources.add(sourcesItem);
    return this;
  }

   /**
   * Weather stations used in response
   * @return sources
  **/
  @Schema(description = "Weather stations used in response")
  public List<String> getSources() {
    return sources;
  }

  public void setSources(List<String> sources) {
    this.sources = sources;
  }

  public History data(List<HistoryObj> data) {
    this.data = data;
    return this;
  }

  public History addDataItem(HistoryObj dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<HistoryObj>();
    }
    this.data.add(dataItem);
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @Schema(description = "")
  public List<HistoryObj> getData() {
    return data;
  }

  public void setData(List<HistoryObj> data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    History history = (History) o;
    return Objects.equals(this.cityName, history.cityName) &&
        Objects.equals(this.stateCode, history.stateCode) &&
        Objects.equals(this.countryCode, history.countryCode) &&
        Objects.equals(this.timezone, history.timezone) &&
        Objects.equals(this.lat, history.lat) &&
        Objects.equals(this.lon, history.lon) &&
        Objects.equals(this.sources, history.sources) &&
        Objects.equals(this.data, history.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityName, stateCode, countryCode, timezone, lat, lon, sources, data);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class History {\n");
    
    sb.append("    cityName: ").append(toIndentedString(cityName)).append("\n");
    sb.append("    stateCode: ").append(toIndentedString(stateCode)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("    sources: ").append(toIndentedString(sources)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}