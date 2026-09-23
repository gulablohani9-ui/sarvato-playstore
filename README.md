# Sarvatobhadra App v0.5

This is the next update of the same `sarvato-playstore` repository.

## v0.5
- 9x9 / 81-cell classical Sarvatobhadra layout.
- 28 Nakshatras including Abhijit.
- Rashi, akshara, tithi and weekday cells.
- Natal point highlighting.
- Transit Vedha engine with front/left/right rays.
- Motion rule: normal=front, fast=left, retrograde=right for Mars/Mercury/Jupiter/Venus/Saturn.
- Sun/Moon/Rahu/Ketu use all three directions in the selected rule set.
- Benefic/malefic classification.
- Real Swiss-Ephemeris-backed FastAPI service.
- Fixed GitHub Actions workflow: setup-android v4, checkout v6, setup-java v5, setup-gradle v6, Gradle 8.7.

## Build
The workflow can build a debug APK without a Gradle wrapper by installing Gradle 8.7 through setup-gradle.

For the real planetary results, deploy `ephemeris-server` and pass its URL to the Android build as `EPHEMERIS_BASE_URL` in a later production configuration. Do not put private API keys in source.

## Licensing
Swiss Ephemeris is separately licensed software. Before commercial distribution, review its AGPL/Professional licensing terms and comply with the selected license.

## Sources / rule note
The published Sarvatobhadra figure used here is consistent with the 9x9 chart reproduced in Vedic astrology references. Vedha motion rules vary by textual tradition, so this version labels the selected rule as the Mansagari-style rule rather than claiming all traditions use identical rules.
