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
import java.math.BigDecimal;
import java.util.Objects;
/**
 * HistoryObj
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-07-23T18:38:21.490044+08:00[Asia/Shanghai]")
public class HistoryObj {
  @SerializedName("ts")
  private BigDecimal ts = null;

  @SerializedName("timestamp_local")
  private String timestampLocal = null;

  @SerializedName("timestamp_utc")
  private String timestampUtc = null;

  @SerializedName("datetime")
  private String datetime = null;

  @SerializedName("slp")
  private BigDecimal slp = null;

  @SerializedName("pres")
  private BigDecimal pres = null;

  @SerializedName("rh")
  private Integer rh = null;

  @SerializedName("dewpt")
  private Integer dewpt = null;

  @SerializedName("temp")
  private BigDecimal temp = null;

  @SerializedName("wind_spd")
  private BigDecimal windSpd = null;

  @SerializedName("wind_dir")
  private Integer windDir = null;

  @SerializedName("uv")
  private Integer uv = null;

  @SerializedName("solar_rad")
  private BigDecimal solarRad = null;

  @SerializedName("ghi")
  private BigDecimal ghi = null;

  @SerializedName("dhi")
  private BigDecimal dhi = null;

  @SerializedName("dni")
  private BigDecimal dni = null;

  @SerializedName("h_angle")
  private BigDecimal hAngle = null;

  @SerializedName("elev_angle")
  private BigDecimal elevAngle = null;

  @SerializedName("pod")
  private String pod = null;

  @SerializedName("weather")
  private Object weather = null;

  @SerializedName("precip")
  private BigDecimal precip = null;

  @SerializedName("snow")
  private BigDecimal snow = null;

  @SerializedName("snow6h")
  private BigDecimal snow6h = null;

  public HistoryObj ts(BigDecimal ts) {
    this.ts = ts;
    return this;
  }

   /**
   * Unix Timestamp
   * @return ts
  **/
  @Schema(example = "1.5517188E+9", description = "Unix Timestamp")
  public BigDecimal getTs() {
    return ts;
  }

  public void setTs(BigDecimal ts) {
    this.ts = ts;
  }

  public HistoryObj timestampLocal(String timestampLocal) {
    this.timestampLocal = timestampLocal;
    return this;
  }

   /**
   * Timestamp in local time
   * @return timestampLocal
  **/
  @Schema(example = "2019-03-04T12:00:00", description = "Timestamp in local time")
  public String getTimestampLocal() {
    return timestampLocal;
  }

  public void setTimestampLocal(String timestampLocal) {
    this.timestampLocal = timestampLocal;
  }

  public HistoryObj timestampUtc(String timestampUtc) {
    this.timestampUtc = timestampUtc;
    return this;
  }

   /**
   * Timestamp UTC
   * @return timestampUtc
  **/
  @Schema(example = "2019-03-04T17:00:00", description = "Timestamp UTC")
  public String getTimestampUtc() {
    return timestampUtc;
  }

  public void setTimestampUtc(String timestampUtc) {
    this.timestampUtc = timestampUtc;
  }

  public HistoryObj datetime(String datetime) {
    this.datetime = datetime;
    return this;
  }

   /**
   * Date in format \&quot;YYYY-MM-DD:HH\&quot;. All datetime is in (UTC)
   * @return datetime
  **/
  @Schema(example = "2019-03-04:17", description = "Date in format \"YYYY-MM-DD:HH\". All datetime is in (UTC)")
  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public HistoryObj slp(BigDecimal slp) {
    this.slp = slp;
    return this;
  }

   /**
   * Sea level pressure (mb)
   * @return slp
  **/
  @Schema(example = "1020.1", description = "Sea level pressure (mb)")
  public BigDecimal getSlp() {
    return slp;
  }

  public void setSlp(BigDecimal slp) {
    this.slp = slp;
  }

  public HistoryObj pres(BigDecimal pres) {
    this.pres = pres;
    return this;
  }

   /**
   * Pressure (mb)
   * @return pres
  **/
  @Schema(example = "845.0", description = "Pressure (mb)")
  public BigDecimal getPres() {
    return pres;
  }

  public void setPres(BigDecimal pres) {
    this.pres = pres;
  }

  public HistoryObj rh(Integer rh) {
    this.rh = rh;
    return this;
  }

   /**
   * Relative Humidity as a percentage (%)
   * @return rh
  **/
  @Schema(example = "85", description = "Relative Humidity as a percentage (%)")
  public Integer getRh() {
    return rh;
  }

  public void setRh(Integer rh) {
    this.rh = rh;
  }

  public HistoryObj dewpt(Integer dewpt) {
    this.dewpt = dewpt;
    return this;
  }

   /**
   * Dew point (Default Celcius)
   * @return dewpt
  **/
  @Schema(description = "Dew point (Default Celcius)")
  public Integer getDewpt() {
    return dewpt;
  }

  public void setDewpt(Integer dewpt) {
    this.dewpt = dewpt;
  }

  public HistoryObj temp(BigDecimal temp) {
    this.temp = temp;
    return this;
  }

   /**
   * Temperature (Default Celcius)
   * @return temp
  **/
  @Schema(example = "-1.2", description = "Temperature (Default Celcius)")
  public BigDecimal getTemp() {
    return temp;
  }

  public void setTemp(BigDecimal temp) {
    this.temp = temp;
  }

  public HistoryObj windSpd(BigDecimal windSpd) {
    this.windSpd = windSpd;
    return this;
  }

   /**
   * Wind Speed (Default m/s)
   * @return windSpd
  **/
  @Schema(example = "14.7", description = "Wind Speed (Default m/s)")
  public BigDecimal getWindSpd() {
    return windSpd;
  }

  public void setWindSpd(BigDecimal windSpd) {
    this.windSpd = windSpd;
  }

  public HistoryObj windDir(Integer windDir) {
    this.windDir = windDir;
    return this;
  }

   /**
   * Wind direction (Degrees)
   * @return windDir
  **/
  @Schema(example = "325", description = "Wind direction (Degrees)")
  public Integer getWindDir() {
    return windDir;
  }

  public void setWindDir(Integer windDir) {
    this.windDir = windDir;
  }

  public HistoryObj uv(Integer uv) {
    this.uv = uv;
    return this;
  }

   /**
   * UV Index (1-11+)
   * @return uv
  **/
  @Schema(example = "4", description = "UV Index (1-11+)")
  public Integer getUv() {
    return uv;
  }

  public void setUv(Integer uv) {
    this.uv = uv;
  }

  public HistoryObj solarRad(BigDecimal solarRad) {
    this.solarRad = solarRad;
    return this;
  }

   /**
   * Estimated solar radiation (W/m^2)
   * @return solarRad
  **/
  @Schema(example = "300.0", description = "Estimated solar radiation (W/m^2)")
  public BigDecimal getSolarRad() {
    return solarRad;
  }

  public void setSolarRad(BigDecimal solarRad) {
    this.solarRad = solarRad;
  }

  public HistoryObj ghi(BigDecimal ghi) {
    this.ghi = ghi;
    return this;
  }

   /**
   * Global horizontal solar irradiance (W/m^2)
   * @return ghi
  **/
  @Schema(example = "1500.0", description = "Global horizontal solar irradiance (W/m^2)")
  public BigDecimal getGhi() {
    return ghi;
  }

  public void setGhi(BigDecimal ghi) {
    this.ghi = ghi;
  }

  public HistoryObj dhi(BigDecimal dhi) {
    this.dhi = dhi;
    return this;
  }

   /**
   * Diffuse normal solar irradiance (W/m^2)
   * @return dhi
  **/
  @Schema(example = "200.0", description = "Diffuse normal solar irradiance (W/m^2)")
  public BigDecimal getDhi() {
    return dhi;
  }

  public void setDhi(BigDecimal dhi) {
    this.dhi = dhi;
  }

  public HistoryObj dni(BigDecimal dni) {
    this.dni = dni;
    return this;
  }

   /**
   * Direct normal solar irradiance (W/m^2)
   * @return dni
  **/
  @Schema(example = "400.0", description = "Direct normal solar irradiance (W/m^2)")
  public BigDecimal getDni() {
    return dni;
  }

  public void setDni(BigDecimal dni) {
    this.dni = dni;
  }

  public HistoryObj hAngle(BigDecimal hAngle) {
    this.hAngle = hAngle;
    return this;
  }

   /**
   * Solar hour angle (Degrees)
   * @return hAngle
  **/
  @Schema(example = "15.0", description = "Solar hour angle (Degrees)")
  public BigDecimal getHAngle() {
    return hAngle;
  }

  public void setHAngle(BigDecimal hAngle) {
    this.hAngle = hAngle;
  }

  public HistoryObj elevAngle(BigDecimal elevAngle) {
    this.elevAngle = elevAngle;
    return this;
  }

   /**
   * Solar elevation angle (Degrees)
   * @return elevAngle
  **/
  @Schema(example = "27.5", description = "Solar elevation angle (Degrees)")
  public BigDecimal getElevAngle() {
    return elevAngle;
  }

  public void setElevAngle(BigDecimal elevAngle) {
    this.elevAngle = elevAngle;
  }

  public HistoryObj pod(String pod) {
    this.pod = pod;
    return this;
  }

   /**
   * Part of the day (d &#x3D; day, n &#x3D; night)
   * @return pod
  **/
  @Schema(example = "n", description = "Part of the day (d = day, n = night)")
  public String getPod() {
    return pod;
  }

  public void setPod(String pod) {
    this.pod = pod;
  }

  public HistoryObj weather(Object weather) {
    this.weather = weather;
    return this;
  }

   /**
   * Get weather
   * @return weather
  **/
  @Schema(description = "")
  public Object getWeather() {
    return weather;
  }

  public void setWeather(Object weather) {
    this.weather = weather;
  }

  public HistoryObj precip(BigDecimal precip) {
    this.precip = precip;
    return this;
  }

   /**
   * Liquid equivalent precipitation - Default (mm)
   * @return precip
  **/
  @Schema(example = "3.0", description = "Liquid equivalent precipitation - Default (mm)")
  public BigDecimal getPrecip() {
    return precip;
  }

  public void setPrecip(BigDecimal precip) {
    this.precip = precip;
  }

  public HistoryObj snow(BigDecimal snow) {
    this.snow = snow;
    return this;
  }

   /**
   * Snowfall - Default (mm)
   * @return snow
  **/
  @Schema(example = "30.0", description = "Snowfall - Default (mm)")
  public BigDecimal getSnow() {
    return snow;
  }

  public void setSnow(BigDecimal snow) {
    this.snow = snow;
  }

  public HistoryObj snow6h(BigDecimal snow6h) {
    this.snow6h = snow6h;
    return this;
  }

   /**
   * Snowfall in last 6 hours - Default (mm)
   * @return snow6h
  **/
  @Schema(example = "60.0", description = "Snowfall in last 6 hours - Default (mm)")
  public BigDecimal getSnow6h() {
    return snow6h;
  }

  public void setSnow6h(BigDecimal snow6h) {
    this.snow6h = snow6h;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryObj historyObj = (HistoryObj) o;
    return Objects.equals(this.ts, historyObj.ts) &&
        Objects.equals(this.timestampLocal, historyObj.timestampLocal) &&
        Objects.equals(this.timestampUtc, historyObj.timestampUtc) &&
        Objects.equals(this.datetime, historyObj.datetime) &&
        Objects.equals(this.slp, historyObj.slp) &&
        Objects.equals(this.pres, historyObj.pres) &&
        Objects.equals(this.rh, historyObj.rh) &&
        Objects.equals(this.dewpt, historyObj.dewpt) &&
        Objects.equals(this.temp, historyObj.temp) &&
        Objects.equals(this.windSpd, historyObj.windSpd) &&
        Objects.equals(this.windDir, historyObj.windDir) &&
        Objects.equals(this.uv, historyObj.uv) &&
        Objects.equals(this.solarRad, historyObj.solarRad) &&
        Objects.equals(this.ghi, historyObj.ghi) &&
        Objects.equals(this.dhi, historyObj.dhi) &&
        Objects.equals(this.dni, historyObj.dni) &&
        Objects.equals(this.hAngle, historyObj.hAngle) &&
        Objects.equals(this.elevAngle, historyObj.elevAngle) &&
        Objects.equals(this.pod, historyObj.pod) &&
        Objects.equals(this.weather, historyObj.weather) &&
        Objects.equals(this.precip, historyObj.precip) &&
        Objects.equals(this.snow, historyObj.snow) &&
        Objects.equals(this.snow6h, historyObj.snow6h);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ts, timestampLocal, timestampUtc, datetime, slp, pres, rh, dewpt, temp, windSpd, windDir, uv, solarRad, ghi, dhi, dni, hAngle, elevAngle, pod, weather, precip, snow, snow6h);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryObj {\n");
    
    sb.append("    ts: ").append(toIndentedString(ts)).append("\n");
    sb.append("    timestampLocal: ").append(toIndentedString(timestampLocal)).append("\n");
    sb.append("    timestampUtc: ").append(toIndentedString(timestampUtc)).append("\n");
    sb.append("    datetime: ").append(toIndentedString(datetime)).append("\n");
    sb.append("    slp: ").append(toIndentedString(slp)).append("\n");
    sb.append("    pres: ").append(toIndentedString(pres)).append("\n");
    sb.append("    rh: ").append(toIndentedString(rh)).append("\n");
    sb.append("    dewpt: ").append(toIndentedString(dewpt)).append("\n");
    sb.append("    temp: ").append(toIndentedString(temp)).append("\n");
    sb.append("    windSpd: ").append(toIndentedString(windSpd)).append("\n");
    sb.append("    windDir: ").append(toIndentedString(windDir)).append("\n");
    sb.append("    uv: ").append(toIndentedString(uv)).append("\n");
    sb.append("    solarRad: ").append(toIndentedString(solarRad)).append("\n");
    sb.append("    ghi: ").append(toIndentedString(ghi)).append("\n");
    sb.append("    dhi: ").append(toIndentedString(dhi)).append("\n");
    sb.append("    dni: ").append(toIndentedString(dni)).append("\n");
    sb.append("    hAngle: ").append(toIndentedString(hAngle)).append("\n");
    sb.append("    elevAngle: ").append(toIndentedString(elevAngle)).append("\n");
    sb.append("    pod: ").append(toIndentedString(pod)).append("\n");
    sb.append("    weather: ").append(toIndentedString(weather)).append("\n");
    sb.append("    precip: ").append(toIndentedString(precip)).append("\n");
    sb.append("    snow: ").append(toIndentedString(snow)).append("\n");
    sb.append("    snow6h: ").append(toIndentedString(snow6h)).append("\n");
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
