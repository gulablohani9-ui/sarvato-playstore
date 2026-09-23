Build fixes included:
- Removed duplicate PlanetPosition and AstrologyConstants declarations.
- Fixed Kotlin getter syntax in the astrology model.
- Kept latitude in PlanetPosition and used named arguments in EphemerisApi.
- Enabled Android BuildConfig and defined EPHEMERIS_BASE_URL from the Gradle property.
- Java/Kotlin JVM target remains 17.
- Android SDK setup uses setup-android v4.
