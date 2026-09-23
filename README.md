# Sarvatobhadra App v0.3

This is an update of the same Android project, not a separate app.

## What changed
- Removed the fake/demo planetary calculation path.
- Added `EphemerisProvider` abstraction.
- Added `HttpEphemerisProvider` for a real Swiss-Ephemeris-backed calculation service.
- Requests Lahiri sidereal positions with date/time, timezone, latitude and longitude.
- UI refuses to show fake planetary positions if the production endpoint is not configured.

## Important licensing
Swiss Ephemeris is dual-licensed: AGPL or Swiss Ephemeris Professional License. If you distribute an app containing Swiss Ephemeris, choose and comply with the applicable license before distribution. See the official Swiss Ephemeris license and documentation.

## Next
1. Deploy/choose a properly licensed ephemeris backend or integrate a properly licensed native implementation.
2. Set `endpoint` in `MainActivity.kt`.
3. Add strict JSON schema validation and test vectors.
4. Then implement Sarvatobhadra placement, Vedha, transit, Navatara and Upagraha.

This project does NOT copy the source code, branding or artwork of any other astrology application.
