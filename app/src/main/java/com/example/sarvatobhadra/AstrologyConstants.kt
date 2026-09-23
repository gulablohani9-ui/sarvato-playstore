package com.example.sarvatobhadra
object AstrologyConstants {
 val nakshatras=listOf("Ashwini","Bharani","Krittika","Rohini","Mrigashira","Ardra","Punarvasu","Pushya","Ashlesha","Magha","Purva Phalguni","Uttara Phalguni","Hasta","Chitra","Swati","Vishakha","Anuradha","Jyeshtha","Mula","Purva Ashadha","Uttara Ashadha","Abhijit","Shravana","Dhanishtha","Shatabhisha","Purva Bhadra","Uttara Bhadra","Revati")
 val rashis=listOf("Mesha","Vrishabha","Mithuna","Karka","Simha","Kanya","Tula","Vrishchika","Dhanu","Makara","Kumbha","Meena")
}
data class PlanetPosition(val planet:String,val longitude:Double,val speed:Double=0.0,val retrograde:Boolean=false){
 val rashi:Int get()=(longitude/30).toInt()%12+1
 val nakshatraIndex:Int get()=(longitude/(360.0/27.0)).toInt().coerceIn(0,26)
 val pada:Int get((((longitude%(360.0/27.0))/(360.0/108.0)).toInt()+1).coerceIn(1,4)
}
