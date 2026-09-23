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
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(12,12,12,18)}
  val scroll=ScrollView(this);scroll.addView(root)
  root.addView(TextView(this).apply{text="सर्वतोभद्र चक्र  v0.4";textSize=24f;gravity=Gravity.CENTER;setTextColor(Color.rgb(80,25,100));setPadding(0,8,0,14)})
  fun e(h:String)=EditText(this).apply{hint=h;setSingleLine(true)}
  val date=e("Date DD-MM-YYYY");val time=e("Time HH:MM");val lat=e("Latitude");val lon=e("Longitude")
  listOf(date,time,lat,lon).forEach(root::addView)
  val btn=Button(this).apply{text="Generate Chakra"};root.addView(btn)
  val grid=GridLayout(this).apply{columnCount=9;rowCount=9;alignmentMode=GridLayout.ALIGN_BOUNDS}
  val labels=SarvatobhadraGrid.cells
  for(r in 0..8)for(c in 0..8){
   val t=TextView(this).apply{text=labels[r][c].label;gravity=Gravity.CENTER;textSize=if(labels[r][c].label.length>8)8f else 10f;setPadding(1,3,1,3);setBackgroundColor(Color.rgb(250,247,252))}
   grid.addView(t,GridLayout.LayoutParams().apply{width=0;height=68;columnSpec=GridLayout.spec(c,1,1f);rowSpec=GridLayout.spec(r,1,1f)})
  }
  root.addView(grid)
  val result=TextView(this).apply{textSize=13f;setPadding(4,14,4,8)};root.addView(result)
  btn.setOnClickListener{
   val base=BuildConfig.EPHEMERIS_BASE_URL
   val la=lat.text.toString().toDoubleOrNull();val lo=lon.text.toString().toDoubleOrNull()
   if(base.isBlank()){result.text="Chakra UI तैयार है। Real planetary calculation के लिए EPHEMERIS_BASE_URL सेट करना बाकी है.";return@setOnClickListener}
   if(la==null||lo==null){result.text="Latitude/Longitude सही भरें.";return@setOnClickListener}
   btn.isEnabled=false;result.text="Real ग्रह positions निकाली जा रही हैं…"
   scope.launch{
    val iso="${date.text}T${time.text}"
    val out=EphemerisApi(base).chart(iso,"Asia/Kolkata",la,lo)
    btn.isEnabled=true
    result.text=out.fold({ps->ps.joinToString("\n"){p->"${p.planet}: ${"%.2f".format(p.longitude)}°  ${AstrologyConstants.nakshatras[p.nakshatraIndex]} P${p.pada}"}+"

अब अगला चरण: Vedha rays + natal points."},{e->"Calculation error: ${e.message}"})
   }
  }
  setContentView(scroll)
 }
}
