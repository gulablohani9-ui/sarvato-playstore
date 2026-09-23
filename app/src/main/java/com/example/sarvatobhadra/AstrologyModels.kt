package com.example.sarvatobhadra

data class PlanetPosition(
    val planet: String,
    val longitude: Double,
    val retrograde: Boolean = false
) {
    val rashi: Int get() = ((longitude / 30.0).toInt() % 12) + 1
    val degreeInRashi: Double get() = longitude % 30.0
    val nakshatraIndex: Int get() = (longitude / (360.0 / 27.0)).toInt().coerceIn(0,26)
    val pada: Int get() = (((longitude % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() + 1).coerceIn(1,4)
}

object AstrologyConstants {
    val nakshatras = listOf(
        "Ashwini","Bharani","Krittika","Rohini","Mrigashira","Ardra","Punarvasu","Pushya","Ashlesha",
        "Magha","Purva Phalguni","Uttara Phalguni","Hasta","Chitra","Swati","Vishakha","Anuradha",
        "Jyeshtha","Mula","Purva Ashadha","Uttara Ashadha","Abhijit","Shravana","Dhanishtha",
        "Shatabhisha","Purva Bhadrapada","Uttara Bhadrapada","Revati"
    )
    val rashis = listOf("Mesha","Vrishabha","Mithuna","Karka","Simha","Kanya","Tula","Vrishchika","Dhanu","Makara","Kumbha","Meena")
}

interface EphemerisProvider {
    fun calculate(dateTimeIso: String, latitude: Double, longitude: Double, timezone: String): List<PlanetPosition>
}

/**
 * Temporary provider used by the UI until the production ephemeris engine is connected.
 * Keeping this interface separate prevents fake values from being mistaken for astronomical results.
 */
class DemoEphemerisProvider : EphemerisProvider {
    override fun calculate(dateTimeIso: String, latitude: Double, longitude: Double, timezone: String): List<PlanetPosition> =
        listOf(
            PlanetPosition("Sun", 120.0),
            PlanetPosition("Moon", 195.0),
            PlanetPosition("Mars", 75.0),
            PlanetPosition("Mercury", 110.0),
            PlanetPosition("Jupiter", 45.0),
            PlanetPosition("Venus", 150.0),
            PlanetPosition("Saturn", 310.0),
            PlanetPosition("Rahu", 220.0, true),
            PlanetPosition("Ketu", 40.0, true)
        )
}
