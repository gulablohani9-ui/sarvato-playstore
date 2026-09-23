# Sarvatobhadra App v0.4

**Same project / same repository — update only.**

### Added
- Classical-style 9x9 Sarvatobhadra grid structure.
- 28 nakshatra outer ring including Abhijit.
- Rashi, akshara and grouped tithi layers.
- Real Swiss Ephemeris-backed API client.
- Included a small FastAPI + pyswisseph backend under `ephemeris-server/`.
- GitHub Actions workflow that builds a debug APK automatically.

### Backend
Run:
`cd ephemeris-server`
`pip install -r requirements.txt`
`uvicorn main:app --host 0.0.0.0 --port 8080`

Then build Android with:
`./gradlew assembleDebug -PEPHEMERIS_BASE_URL=http://YOUR_SERVER:8080`

For a public/commercial deployment, review Swiss Ephemeris licensing. The Swiss Ephemeris project is dual-licensed; the AGPL path has source-sharing obligations for covered deployments.

### Important
This version implements the calculation plumbing and grid. The exact Sarvatobhadra Vedha-ray rules and name-akshara mapping are kept as the next module because traditions and published layouts differ in some details. Do not market this version as a finished replica of another app.
