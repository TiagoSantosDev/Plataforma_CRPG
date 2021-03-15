package com.github.vipulasri.timelineview.sample

import androidx.lifecycle.ViewModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.google.gson.Gson

class JSONMealParser : ViewModel() {
    val TAG = "JSONParser"

    val m = Meal("a", "b", "c", "d")

    private fun convertMealsToJSON() {

        val gson = Gson()
        val json = gson.toJson(m)
        println(json)
    }

    private fun getMealsFromJSON() {
    }
}
