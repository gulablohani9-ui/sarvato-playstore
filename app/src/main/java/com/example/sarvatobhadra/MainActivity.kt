package com.example.sarvatobhadra

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Cell(val text: String, val ring: Int)

class MainActivity : AppCompatActivity() {
    private val grid = arrayOf(
        arrayOf("ई","धनिष्ठा","शतभिषा","पूर्व भाद्र","उत्तर भाद्र","रेवती","अश्विनी","भरणी","अ"),
        arrayOf("श्रवण","ऋ","ग","स","द","च","ल","उ","कृत्तिका"),
        arrayOf("अभिजित","ख","ऐ","कुंभ","मीन","मेष","लृ","अ","रोहिणी"),
        arrayOf("उत्तराषाढ़ा","ज","मकर","अः","शुक्र","ओ","वृष","व","मृगशीर्ष"),
        arrayOf("पूर्वाषाढ़ा","भ","धनु","गुरु","शनि","रवि/मंगल","मिथुन","क","आर्द्रा"),
        arrayOf("मूल","य","वृश्चिक","अं","सोम/बुध","औ","कर्क","ह","पुनर्वसु"),
        arrayOf("ज्येष्ठा","न","ए","तुला","कन्या","सिंह","लृ","ड","पुष्य"),
        arrayOf("अनुराधा","ऋ","त","र","प","ट","म","ऊ","आश्लेषा"),
        arrayOf("इ","विशाखा","स्वाती","चित्रा","हस्त","उत्तर फाल्गुनी","पूर्व फाल्गुनी","मघा","आ")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun buildUi() {
        val scroll = ScrollView(this)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18,18,18,24)
        }
        scroll.addView(root)

        val title = TextView(this).apply {
            text = "सर्वतोभद्र चक्र"
            textSize = 25f; gravity = Gravity.CENTER
            setTextColor(Color.rgb(80,25,100)); setPadding(4,10,4,18)
        }
        root.addView(title)

        val form = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        fun field(hint: String): EditText = EditText(this).apply {
            this.hint = hint; setSingleLine(true)
        }
        val name = field("नाम / Name")
        val dob = field("जन्म तिथि  (DD-MM-YYYY)")
        val time = field("जन्म समय  (HH:MM)")
        val place = field("जन्म स्थान")
        form.addView(name); form.addView(dob); form.addView(time); form.addView(place)

        val draw = Button(this).apply { text = "चक्र बनाएं" }
        form.addView(draw)
        root.addView(form)

        val info = TextView(this).apply {
            text = "परंपरागत 9×9 Sarvatobhadra layout — 81 cells"
            textSize = 14f; setPadding(4,12,4,12)
        }
        root.addView(info)

        val table = TableLayout(this).apply {
            isStretchAllColumns = true
            isShrinkAllColumns = true
        }
        for (r in grid.indices) {
            val row = TableRow(this)
            for (c in grid[r].indices) {
                val tv = TextView(this).apply {
                    text = grid[r][c]
                    gravity = Gravity.CENTER
                    textSize = if (grid[r][c].length > 7) 10f else 12f
                    setPadding(2,8,2,8)
                    setTextColor(Color.DKGRAY)
                    setBackgroundColor(Color.rgb(250,247,252))
                }
                row.addView(tv, TableRow.LayoutParams(0, 72, 1f))
            }
            table.addView(row)
        }
        root.addView(table)

        val note = TextView(this).apply {
            text = "नोट: यह पहला working UI है। ग्रहों की astronomical position, Lahiri ayanamsa, natal/transit vedha और Navatara/Upagraha calculations अगले module में जोड़े जाएंगे।"
            textSize = 13f; setPadding(4,16,4,8)
        }
        root.addView(note)

        draw.setOnClickListener {
            Toast.makeText(this, "चक्र तैयार है: ${name.text.ifBlank { "नाम नहीं दिया" }}", Toast.LENGTH_SHORT).show()
        }
        setContentView(scroll)
    }
}
