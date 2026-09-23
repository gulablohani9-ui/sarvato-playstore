# Sarvatobhadra App v0.2
This version adds the astrology data model and deterministic Nakshatra/Rashi mapping layer.
Planetary longitudes are kept behind `EphemerisProvider` so the final app can use a licensed/appropriate Swiss Ephemeris implementation without mixing it into UI code.

References researched: Swiss Ephemeris supports sidereal calculations including Lahiri ayanamsa. See https://github.com/aloistr/swisseph
