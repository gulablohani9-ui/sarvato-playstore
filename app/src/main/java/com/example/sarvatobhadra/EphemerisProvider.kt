package com.example.sarvatobhadra

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class BirthRequest(
    val isoDateTime: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String = "Asia/Kolkata"
)

interface EphemerisProvider {
    suspend fun calculate(request: BirthRequest): Result<List<PlanetPosition>>
}

/**
 * HTTP adapter for a Swiss-Ephemeris-backed service.
 *
 * The Android app does not bundle Swiss Ephemeris binaries here. This keeps the
 * UI independent of the ephemeris implementation and avoids silently treating
 * demo numbers as astronomical results.
 *
 * Expected response shape:
 * {
 *   "positions":[
 *     {"planet":"Sun","longitude":123.45,"latitude":0.1,"speed":0.9,"retrograde":false}
 *   ]
 * }
 */
class HttpEphemerisProvider(
    private val endpoint: String
) : EphemerisProvider {
    override suspend fun calculate(request: BirthRequest): Result<List<PlanetPosition>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val url = URL(endpoint)
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 15000
                    readTimeout = 20000
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }
                val body = JSONObject().apply {
                    put("datetime", request.isoDateTime)
                    put("tz", request.timezone)
                    put("lat", request.latitude)
                    put("lon", request.longitude)
                    put("ayanamsha", "lahiri")
                }.toString()
                conn.outputStream.use { it.write(body.toByteArray()) }
                if (conn.responseCode !in 200..299) error("Ephemeris server HTTP ${conn.responseCode}")
                val text = conn.inputStream.bufferedReader().use { it.readText() }
                val arr = JSONObject(text).getJSONArray("positions")
                buildList {
                    for (i in 0 until arr.length()) {
                        val p = arr.getJSONObject(i)
                        add(PlanetPosition(
                            planet = p.getString("planet"),
                            longitude = p.getDouble("longitude"),
                            latitude = p.optDouble("latitude", 0.0),
                            speed = p.optDouble("speed", 0.0),
                            retrograde = p.optBoolean("retrograde", false)
                        ))
                    }
                }
            }
        }
}
