package com.example.sarvatobhadra

data class SbcCell(val label:String, val type:String)

object SarvatobhadraGrid {
    // 9x9 classical-style nested layout. Outer ring has 28 nakshatras + 4 corner vowels.
    // The exact akshara transliteration varies by textual tradition; labels are kept editable.
    val cells: Array<Array<SbcCell>> = run {
        val a=Array(9){Array(9){SbcCell("","empty")}}
        fun put(r:Int,c:Int,label:String,type:String){a[r][c]=SbcCell(label,type)}
        val outer=listOf(
            "अ","Dhanishtha","Shatabhisha","Purva Bhadra","Uttara Bhadra","Revati","Ashwini","Bharani","अ",
            "Krittika","Rohini","Mrigashira","Ardra","Punarvasu","Pushya","Ashlesha",
            "आ","Magha","Purva Phalguni","Uttara Phalguni","Hasta","Chitra","Swati","Vishakha","आ",
            "Anuradha","Jyeshtha","Mula","Purva Ashadha","Uttara Ashadha","Abhijit","Shravana","इ",
            "इ"
        )
        // Explicit perimeter order: top, right, bottom, left.
        val top=listOf("ई","Dhanishtha","Shatabhisha","Purva Bhadra","Uttara Bhadra","Revati","Ashwini","Bharani","अ")
        val right=listOf("अ","Krittika","Rohini","Mrigashira","Ardra","Punarvasu","Pushya","Ashlesha","आ")
        val bottom=listOf("इ","Vishakha","Swati","Chitra","Hasta","Uttara Phalguni","Purva Phalguni","Magha","आ")
        val left=listOf("ई","Shravana","Abhijit","Uttara Ashadha","Purva Ashadha","Mula","Jyeshtha","Anuradha","इ")
        for(c in 0..8) { put(0,c,top[c],if(c==0||c==8)"vowel" else "nakshatra"); put(8,c,bottom[c],if(c==0||c==8)"vowel" else "nakshatra") }
        for(r in 1..7) { put(r,8,right[r],if(r==8)"vowel" else "nakshatra"); put(r,0,left[r],if(r==0||r==8)"vowel" else "nakshatra") }

        val akTop=listOf("ॠ","t","th","d","dh","n","u")
        val akRight=listOf("l","k","g","ch","j","y","r")
        val akBottom=listOf("p","m","t","u","ri","e","ai")
        val akLeft=listOf("ri","g","s","d","ch","l","u")
        for(c in 1..7) put(1,c,akTop[c-1],"akshara")
        for(r in 2..7) put(r,7,akRight[r-1],"akshara")
        for(c in 1..7) put(7,c,akBottom[c-1],"akshara")
        for(r in 2..7) put(r,1,akLeft[r-1],"akshara")

        val rashisTop=listOf("Aquarius","Pisces","Aries","")
        put(2,2,"Aquarius","rashi");put(2,3,"Pisces","rashi");put(2,4,"Aries","rashi")
        put(2,5,"Taurus","rashi");put(2,6,"Gemini","rashi")
        put(3,6,"Cancer","rashi");put(4,6,"Leo","rashi");put(5,6,"Virgo","rashi")
        put(6,6,"Libra","rashi");put(6,5,"Scorpio","rashi");put(6,4,"Sagittarius","rashi")
        put(6,3,"Capricorn","rashi")

        put(3,3,"Nanda","tithi"); put(3,4,"Bhadra","tithi"); put(3,5,"Jaya","tithi")
        put(4,3,"Rikta","tithi"); put(4,4,"Poorna","tithi"); put(4,5,"Rikta","tithi")
        put(5,3,"Jaya","tithi"); put(5,4,"Bhadra","tithi"); put(5,5,"Nanda","tithi")
        a
    }

    fun cellForNakshatra(index:Int): Pair<Int,Int>? {
        val wanted=AstrologyConstants.nakshatras[index.coerceIn(0,26)]
        for(r in cells.indices) for(c in cells[r].indices)
            if(cells[r][c].label.equals(wanted,ignoreCase=true)) return r to c
        return null
    }
}
