package com.example.lunaris

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_options.*


class activity_options : AppCompatActivity() {

    private var algorithm = Algorithm.TRIG1
    private var hemisphere = "north"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        setCurrentOptions()

        hemisphereGroup.setOnCheckedChangeListener { _, _ ->
            saveLunarisOptions()
        }

        algorithmGroup.setOnCheckedChangeListener { _, _ ->
            saveLunarisOptions()
        }
    }

    override fun onResume() {
        super.onResume()
        setCurrentOptions()

        hemisphereGroup.setOnCheckedChangeListener { _, _ ->
            saveLunarisOptions()
        }

        algorithmGroup.setOnCheckedChangeListener { _, _ ->
            saveLunarisOptions()
        }
    }

    fun setCurrentOptions(){
        readLunarisOptions()
        when(this.algorithm)
        {
            Algorithm.SIMPLE -> simpleRadio.isChecked=true
            Algorithm.CONWAY -> conwayRadio.isChecked=true
            Algorithm.TRIG1 -> trig1Radio.isChecked=true
            Algorithm.TRIG2 ->  trig2Radio.isChecked=true
        }
        when(this.hemisphere)
        {
            "south" -> southRadio.isChecked=true
            "north" -> northRadio.isChecked=true
        }
    }


    fun saveLunarisOptions() {
        val sharedPreference =  getSharedPreferences("Options_prefs",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        var algorithmSelected: String
        if (simpleRadio.isChecked) algorithmSelected="simple"
        else if (conwayRadio.isChecked) algorithmSelected="conway"
        else if (trig1Radio.isChecked) algorithmSelected= "trig1"
        else algorithmSelected="trig2"

        var hemisphereSelected: String
        if (northRadio.isChecked) hemisphereSelected ="south"
        else hemisphereSelected = "north"

        editor.putString("algorithm",algorithmSelected)
        editor.putString("hemisphere",hemisphereSelected)
        editor.commit()
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
