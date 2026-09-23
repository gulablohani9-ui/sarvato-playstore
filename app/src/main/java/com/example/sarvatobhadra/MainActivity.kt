package com.example.sarvatobhadra

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.Locale

class MainActivity : AppCompatActivity() {
    // Replace this with YOUR deployed Swiss-Ephemeris-backed endpoint.
    // Example format: https://your-domain.example/v1/chart
    private val endpoint = "https://YOUR-EPHEMERIS-SERVER.example/v1/chart"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onDestroy() { scope.cancel(); super.onDestroy() }

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); buildUi() }

    private fun buildUi() {
        val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,18,18,24)}
        val scroll=ScrollView(this); scroll.addView(root)
        root.addView(TextView(this).apply{
            text="सर्वतोभद्र चक्र • v0.3"; textSize=24f; gravity=Gravity.CENTER
            setTextColor(Color.rgb(80,25,100)); setPadding(0,8,0,18)
        })
        fun edit(h:String)=EditText(this).apply{hint=h;setSingleLine(true)}
        val date=edit("जन्म तिथि  DD-MM-YYYY")
        val time=edit("जन्म समय  HH:MM")
        val lat=edit("Latitude  (उदा. 26.2389)")
        val lon=edit("Longitude (उदा. 73.0243)")
        val name=edit("नाम / Name")
        listOf(date,time,lat,lon,name).forEach(root::addView)
        val button=Button(this).apply{text="वास्तविक ग्रह गणना करें"}
        root.addView(button)
        val result=TextView(this).apply{textSize=14f;setPadding(4,14,4,14)}
        root.addView(result)

        button.setOnClickListener {
            val la=lat.text.toString().toDoubleOrNull()
            val lo=lon.text.toString().toDoubleOrNull()
            if (la==null || lo==null) {
                result.text="कृपया Latitude और Longitude सही भरें।"; return@setOnClickListener
            }
            if (endpoint.contains("YOUR-EPHEMERIS-SERVER")) {
                result.text="अभी production ephemeris server connect नहीं किया गया है।\n\nपहले अपना Swiss-Ephemeris-backed endpoint सेट करें।\n\nयहाँ कोई fake planetary position नहीं दिखाई जाएगी।"
                return@setOnClickListener
            }
            button.isEnabled=false
            result.text="ग्रहों की गणना हो रही है…"
            scope.launch {
                val req=BirthRequest("${date.text}T${time.text}",la,lo,"Asia/Kolkata")
                val out=HttpEphemerisProvider(endpoint).calculate(req)
                button.isEnabled=true
                result.text=out.fold(
                    onSuccess={positions -> formatPositions(positions)},
                    onFailure={e -> "Calculation error: ${e.message ?: "unknown error"}"}
                )
            }
        }
        setContentView(scroll)
    }

    private fun formatPositions(ps: List<PlanetPosition>) = buildString {
        append("Sidereal • Lahiri\n\n")
        ps.forEach { p ->
            val n=AstrologyConstants.nakshatras[p.nakshatraIndex]
            val r=AstrologyConstants.rashis[p.rashi-1]
            append(String.format(Locale.US,"%s • %s %.2f° • %s • Pada %d%s\n",
                p.planet,r,p.degreeInRashi,n,p.pada,if(p.retrograde)" ℞" else ""))
        }
        append("\nअगला module: Sarvatobhadra placement + Vedha.")
    }
}
