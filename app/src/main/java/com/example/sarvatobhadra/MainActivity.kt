package com.example.sarvatobhadra
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity:AppCompatActivity(){
 private val scope=CoroutineScope(SupervisorJob()+Dispatchers.Main)
 override fun onDestroy(){scope.cancel();super.onDestroy()}
 override fun onCreate(b:Bundle?){super.onCreate(b);ui()}
 private fun ui(){
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(10,10,10,20)}
  val scroll=ScrollView(this);scroll.addView(root)
  root.addView(TextView(this).apply{text="सर्वतोभद्र चक्र • v0.5";textSize=24f;gravity=Gravity.CENTER;setTextColor(Color.rgb(80,25,100));setPadding(0,8,0,12)})
  fun e(h:String)=EditText(this).apply{hint=h;setSingleLine(true)}
  val date=e("Birth Date  DD-MM-YYYY");val time=e("Birth Time  HH:MM");val lat=e("Latitude");val lon=e("Longitude");val name=e("Name / Naam Akshara")
  listOf(date,time,lat,lon,name).forEach(root::addView)
  val btn=Button(this).apply{text="Generate Natal + Vedha"};root.addView(btn)
  val grid=GridLayout(this).apply{columnCount=9;rowCount=9}
  val views=Array(9){arrayOfNulls<TextView>(9)}
  for(r in 0..8)for(c in 0..8){
   val tv=TextView(this).apply{text=SarvatobhadraEngine.labels[r][c];gravity=Gravity.CENTER;textSize=if(text.length>11)7.5f else 9.5f;setPadding(1,2,1,2);setBackgroundColor(Color.rgb(250,247,252))}
   views[r][c]=tv
   grid.addView(tv,GridLayout.LayoutParams().apply{width=0;height=66;columnSpec=GridLayout.spec(c,1,1f);rowSpec=GridLayout.spec(r,1,1f)})
  }
  root.addView(grid)
  val result=TextView(this).apply{textSize=13f;setPadding(4,14,4,10)};root.addView(result)
  btn.setOnClickListener{
   val base=BuildConfig.EPHEMERIS_BASE_URL
   val la=lat.text.toString().toDoubleOrNull();val lo=lon.text.toString().toDoubleOrNull()
   if(base.isBlank()){result.text="UI + Vedha engine तैयार है। Real ग्रह calculation के लिए GitHub Actions build में EPHEMERIS_BASE_URL secret/property देना होगा.";return@setOnClickListener}
   if(la==null||lo==null){result.text="Latitude और Longitude सही भरें.";return@setOnClickListener}
   btn.isEnabled=false;result.text="Calculation…"
   scope.launch{
    val out=EphemerisApi(base).chart("${date.text}T${time.text}","Asia/Kolkata",la,lo)
    btn.isEnabled=true
    result.text=out.fold({ch->
      val moon=ch.bodies.firstOrNull{it.planet.equals("moon",true)}
      val hits=SarvatobhadraEngine.hits(ch.bodies)
      val natal=SarvatobhadraEngine.natalPoints(moon,null,name.text.toString(),ch.tithi,ch.weekday)
      natal.forEach{views[it.row][it.col]?.setBackgroundColor(Color.rgb(255,235,120))}
      val sb=StringBuilder()
      sb.append("Tithi: ${ch.tithi}   Weekday: ${ch.weekday}\n\n")
      ch.bodies.forEach{p->sb.append("${p.planet}: ${"%.2f".format(p.longitude)}° • ${AstrologyConstants.nakshatras[p.nakshatraIndex]} P${p.pada}${if(p.retrograde)" ℞" else ""}\n")}
      sb.append("\nNatal points:\n");natal.forEach{sb.append("• ${it.label} → ${SarvatobhadraEngine.labels[it.row][it.col]}\n")}
      sb.append("\nVedha hits:\n")
      hits.take(80).forEach{sb.append("• ${it.planet} ${it.direction} → ${it.targetLabel} (${it.nature})\n")}
      sb.toString()
    },{e->"Calculation error: ${e.message}"})
   }
  }
  setContentView(scroll)
 }
}
