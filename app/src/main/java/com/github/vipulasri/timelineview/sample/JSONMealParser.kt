package com.github.vipulasri.timelineview.sample

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.vipulasri.timelineview.sample.model.EventModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.google.gson.Gson
import java.util.ArrayList

class JSONMealParser : ViewModel() {
    val TAG = "JSONParser"

    private fun getMealsFromJSON() {
        val m = Meal("a", "b", "c", "d")
        val gson = Gson()
        val json = gson.toJson(m)
        println(json)
    }
}
