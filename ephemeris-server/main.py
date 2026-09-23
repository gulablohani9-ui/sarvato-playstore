from datetime import datetime
from zoneinfo import ZoneInfo
import swisseph as swe
from fastapi import FastAPI, Query

app=FastAPI(title="Sarvatobhadra Ephemeris",version="0.5.0")
BODIES={"sun":swe.SUN,"moon":swe.MOON,"mercury":swe.MERCURY,"venus":swe.VENUS,"mars":swe.MARS,"jupiter":swe.JUPITER,"saturn":swe.SATURN,"rahu":swe.MEAN_NODE}

@app.get("/health")
def health(): return {"ok":True,"version":"0.5.0"}

@app.get("/v1/chart")
def chart(datetime_iso:str=Query(...,alias="datetime"),tz:str="Asia/Kolkata",lat:float=0,lon:float=0,ayanamsha:str="lahiri",nodes:str="mean"):
    dt=datetime.fromisoformat(datetime_iso.replace("Z","+00:00"))
    if dt.tzinfo is None: dt=dt.replace(tzinfo=ZoneInfo(tz))
    utc=dt.astimezone(ZoneInfo("UTC"))
    jd=swe.julday(utc.year,utc.month,utc.day,utc.hour+utc.minute/60+utc.second/3600)
    swe.set_sid_mode(swe.SIDM_LAHIRI)
    flags=swe.FLG_SWIEPH|swe.FLG_SPEED|swe.FLG_SIDEREAL
    bodies=[]
    for key,body in BODIES.items():
        xx,ret=swe.calc_ut(jd,body,flags)
        bodies.append({"key":key,"sidereal":{"longitude":xx[0]%360,"latitude":xx[1],"speed":xx[3],"retrograde":xx[3]<0}})
    rahu=next(x for x in bodies if x["key"]=="rahu")
    bodies.append({"key":"ketu","sidereal":{"longitude":(rahu["sidereal"]["longitude"]+180)%360,"latitude":0,"speed":rahu["sidereal"]["speed"],"retrograde":True}})
    sun=next(x for x in bodies if x["key"]=="sun")["sidereal"]["longitude"]
    moon=next(x for x in bodies if x["key"]=="moon")["sidereal"]["longitude"]
    diff=(moon-sun)%360
    tithi=int(diff//12)+1
    weekday=utc.weekday() # 0 Mon ... 6 Sun
    return {"meta":{"ayanamsha":"lahiri","timezone":tz,"latitude":lat,"longitude":lon},"tithi":tithi,"weekday":weekday,"bodies":bodies}

@app.get("/")
def root(): return {"service":"Sarvatobhadra Ephemeris","docs":"/docs"}
