package com.example.sarvatobhadra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class EphemerisApi(private val base:String){
 suspend fun chart(datetime:String,tz:String,lat:Double,lon:Double):Result<List<PlanetPosition>>=withContext(Dispatchers.IO){
  runCatching{
   val url=URL(base.trimEnd('/')+"/v1/chart?datetime="+java.net.URLEncoder.encode(datetime,"UTF-8")+"&tz="+java.net.URLEncoder.encode(tz,"UTF-8")+"&lat=$lat&lon=$lon&ayanamsha=lahiri&nodes=mean")
   val c=url.openConnection() as HttpURLConnection
   c.connectTimeout=15000;c.readTimeout=20000;c.requestMethod="GET"
   if(c.responseCode !in 200..299) error("HTTP ${c.responseCode}")
   val o=JSONObject(c.inputStream.bufferedReader().use{it.readText()})
   val arr=o.getJSONArray("bodies")
   buildList {
    for(i in 0 until arr.length()){
     val p=arr.getJSONObject(i)
     val sid=p.getJSONObject("sidereal")
     add(PlanetPosition(p.getString("key"),sid.getDouble("longitude"),sid.optDouble("speed",0.0),sid.optBoolean("retrograde",false)))
    }
   }
  }
 }
}
