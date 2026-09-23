package com.example.sarvatobhadra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class ChartResponse(val bodies:List<PlanetPosition>,val tithi:Int,val weekday:Int)

class EphemerisApi(private val base:String){
 suspend fun chart(datetime:String,tz:String,lat:Double,lon:Double):Result<ChartResponse> = withContext(Dispatchers.IO){
  runCatching{
   val q="v1/chart?datetime=${URLEncoder.encode(datetime,"UTF-8")}&tz=${URLEncoder.encode(tz,"UTF-8")}&lat=$lat&lon=$lon&ayanamsha=lahiri&nodes=mean"
   val c=URL(base.trimEnd('/')+"/"+q).openConnection() as HttpURLConnection
   c.connectTimeout=15000;c.readTimeout=20000;c.requestMethod="GET"
   if(c.responseCode !in 200..299) error("HTTP ${c.responseCode}")
   val o=JSONObject(c.inputStream.bufferedReader().use{it.readText()})
   val arr=o.getJSONArray("bodies")
   val list=buildList{
    for(i in 0 until arr.length()){
     val p=arr.getJSONObject(i);val s=p.getJSONObject("sidereal")
     add(PlanetPosition(p.getString("key"),s.getDouble("longitude"),s.optDouble("speed",0.0),s.optBoolean("retrograde",false)))
    }
   }
   ChartResponse(list,o.optInt("tithi",1),o.optInt("weekday",0))
  }
 }
}
