package com.example.lunaris

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fullmoons.*
import java.time.LocalDate

class activity_fullmoons : AppCompatActivity() {

    private var initYear: Int = 2020
    private var algorithm = Algorithm.TRIG1
    private var hemisphere = "north"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullmoons)

        val yearInputVar= findViewById<EditText>(R.id.yearInput)
        yearInputVar.setText(initYear.toString())

        buttonAdd.setOnClickListener() {
            if (initYear in 1901..2199) {
                initYear++
                yearInputVar.setText(initYear.toString())
                getFullMoons(initYear)
            }
        }

        buttonSubstract.setOnClickListener() {
            if (initYear in 1901..2199) {
                initYear--
                yearInputVar.setText(initYear.toString())
                getFullMoons(initYear)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFullMoons(year: Int){
        val fullMoonsList = mutableListOf<TextView>(
            fullMoon0, fullMoon1, fullMoon2, fullMoon3,
            fullMoon4, fullMoon5, fullMoon6, fullMoon7,
            fullMoon8, fullMoon9, fullMoon10, fullMoon11
        )

        if (year in 1901..2199) {
            readLunarisOptions()
            var calculationsDate = LocalDate.parse(year.toString() + "-01-01")
            var cnt = 0
            while (calculationsDate.year == year) {
                var moonPhase = LunarCalculator().getMoonPhase(algorithm, calculationsDate)
                if (moonPhase == 15.0) {
                    fullMoonsList[cnt].setText(calculationsDate.toString())
                    cnt++
                }
                calculationsDate = calculationsDate.plusDays(1)
            }
        }else for (x in 0..10)  fullMoonsList[x].setText("Wrong year")

    }

    fun readLunarisOptions() {
        val sharedPref = getSharedPreferences("Options_prefs", 0)
        if(sharedPref.contains("algorithm")) {
            val tmpAlgorithm = sharedPref.getString("algorithm", "").toString()
            this.algorithm = when (tmpAlgorithm) {
                "simple" -> Algorithm.SIMPLE
                "conway" -> Algorithm.CONWAY
                "trig1" -> Algorithm.TRIG1
                "trig2" -> Algorithm.TRIG2
                else -> Algorithm.TRIG1
            }
        }

        if(sharedPref.contains("hemisphere")) {
            val tmpHemisphere = sharedPref.getString("hemisphere", "").toString()
            this.hemisphere = tmpHemisphere
        }
    }
}
