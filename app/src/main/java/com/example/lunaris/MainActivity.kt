package com.example.lunaris

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var algorithm=Algorithm.TRIG1
    private var hemisphere="north"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullMoonsButton.setOnClickListener{
            startActivity(Intent(this, activity_fullmoons::class.java))
        }

        optionsButton.setOnClickListener{
            val intent = Intent(this, activity_options::class.java)
            startActivity(intent)
        }

        setDateValues()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setDateValues()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateValues(){

        //SETTING TEXT PARAMETERS IN MAIN APPLICATION
        // #################################
        readLunarisOptions()

        val moonPhasePercent = findViewById<TextView>(R.id.moonPhasePercent)
        val previousFullMoonText = findViewById<TextView>(R.id.previousFullMoonText)
        val nextFullMoonText = findViewById<TextView>(R.id.nextFullMoonText)

        val todayDate = LocalDate.now()
        val calculatedMoonPhase = LunarCalculator().getMoonPhase(algorithm,todayDate)
        val previousFullMoon = LunarCalculator().getPreviousFullMoon(algorithm,todayDate)
        val nextFullMoon = LunarCalculator().getNextFullMoon(algorithm,todayDate)

        moonPhasePercent.text = "Dzisiaj: " + ((calculatedMoonPhase/30)*100).toInt().toString() + "%"
        previousFullMoonText.text= "Poprzedni nów: " + previousFullMoon.toString()
        nextFullMoonText.text = "Następna pełnia: "  + nextFullMoon.toString()

        // SETTING IMAGE IN MAIN APPLICATION
        // #################################
        val imageName = when (this.hemisphere) {
            "north" -> "n" + (29 - calculatedMoonPhase).toInt().toString()
            "south" -> "s" + calculatedMoonPhase.toInt().toString()
            else -> "n1"
        }

        val imageFile: Int = resources.getIdentifier(imageName, "drawable", getPackageName())
        val image = findViewById<ImageView>(R.id.moonPhaseImage)
        image.setImageResource(imageFile)
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

