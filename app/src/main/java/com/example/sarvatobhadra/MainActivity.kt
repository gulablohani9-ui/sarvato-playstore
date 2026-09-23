package com.example.sarvatobhadra

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val ephemeris: EphemerisProvider = DemoEphemerisProvider()

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); buildUi() }

    private fun buildUi() {
        val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,18,18,24)}
        val scroll=ScrollView(this); scroll.addView(root)

        root.addView(TextView(this).apply{
            text="सर्वतोभद्र चक्र • v0.2"; textSize=24f; gravity=Gravity.CENTER
            setTextColor(Color.rgb(80,25,100)); setPadding(0,8,0,18)
        })

        fun edit(h:String)=EditText(this).apply{hint=h;setSingleLine(true)}
        val date=edit("जन्म तिथि  DD-MM-YYYY")
        val time=edit("जन्म समय  HH:MM")
        val lat=edit("Latitude  (उदा. 26.2389)")
        val lon=edit("Longitude (उदा. 73.0243)")
        val name=edit("नाम / Name")
        listOf(date,time,lat,lon,name).forEach(root::addView)

        val button=Button(this).apply{text="गणना करें"}
        root.addView(button)

        val result=TextView(this).apply{textSize=14f;setPadding(4,14,4,14)}
        root.addView(result)

        button.setOnClickListener {
            val positions=ephemeris.calculate("${date.text}T${time.text}", lat.text.toString().toDoubleOrNull()?:26.2389,
                lon.text.toString().toDoubleOrNull()?:73.0243, "Asia/Kolkata")
            result.text=buildString {
                append("Sidereal mode: Lahiri\n\n")
                positions.forEach { p ->
                    val n=AstrologyConstants.nakshatras[p.nakshatraIndex]
                    val r=AstrologyConstants.rashis[p.rashi-1]
                    append(String.format(Locale.US,"%s  •  %s %.2f°  •  %s  Pada %d%s\n",
                        p.planet,r,p.degreeInRashi,n,p.pada,if(p.retrograde)"  ℞" else ""))
                }
                append("\nNext: production ephemeris → exact planetary longitudes → SarvatoBhadra placement → Vedha.")
            }
        }
        setContentView(scroll)
    }
}
