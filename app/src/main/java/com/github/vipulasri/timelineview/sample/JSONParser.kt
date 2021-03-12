package com.github.vipulasri.timelineview.sample

import android.R.attr.data
import androidx.appcompat.app.AppCompatActivity
import com.github.vipulasri.timelineview.sample.model.Meal
import com.google.gson.Gson
import java.io.File

class JSONParser : AppCompatActivity() {

    val meal = Meal("almondegas", "sardinhas", "sopa", "tofu")

    val file = File("meal")

    // file.writeText(jsonString)

    var gson = Gson()
    var json = gson.toJson(data)
}
