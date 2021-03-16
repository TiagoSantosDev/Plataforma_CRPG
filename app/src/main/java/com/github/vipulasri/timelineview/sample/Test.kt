package com.github.vipulasri.timelineview.sample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.github.vipulasri.timelineview.sample.model.Tutorial
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException

@SuppressLint("StaticFieldLeak")
class Test(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    fun a(){
        val json = """{"title": "Kotlin Tutorial #1", "author": "bezkoder", "categories" : ["Kotlin","Basic"]}"""
        val jsonMeal =  """{"carne": "a", "peixe": "b", "dieta" : "c", "vegetariano": "d"}"""
        val gson = Gson()

        val tutorial_1: Tutorial = gson.fromJson(json, Tutorial::class.java)
        println("> From JSON String:\n" + tutorial_1)

        val tutorial_2: Meal = gson.fromJson(jsonMeal, Meal::class.java)
        println("> From JSON Meal String:\n" + tutorial_2)

    }

}
