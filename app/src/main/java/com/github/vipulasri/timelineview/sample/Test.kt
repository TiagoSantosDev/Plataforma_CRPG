package com.github.vipulasri.timelineview.sample

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.github.vipulasri.timelineview.sample.model.Person
import com.github.vipulasri.timelineview.sample.model.Tutorial
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader

@SuppressLint("StaticFieldLeak")
class Test(application: Application) : AndroidViewModel(application) {

    public val context = application.applicationContext

    fun a(){
        val json = """{"title": "Kotlin Tutorial #1", "author": "bezkoder", "categories" : ["Kotlin","Basic"]}"""
        val jsonMeal =  """{"carne": "a", "peixe": "b", "dieta" : "c", "vegetariano": "d"}"""
        val gson = Gson()

        val tutorial_1: Tutorial = gson.fromJson(json, Tutorial::class.java)
        println("> From JSON String:\n" + tutorial_1)

        val tutorial_2: Meal = gson.fromJson(jsonMeal, Meal::class.java)
        println("> From JSON Meal String:\n" + tutorial_2)

    }

    fun b(){

        val gson = Gson()
        val filename = "test.json"
        val fullFilename = context.filesDir.toString() + "/" + filename
        val fileContent = "{\n" +
                "\t\t\"title\": \"Kotlin Tutorial #2\",\n" +
                "\t\t\"author\": \"bezkoder\",\n" +
                "\t\t\"categories\": [\n" +
                "\t\t\t\"Kotlin\",\n" +
                "\t\t\t\"Basic\"\n" +
                "\t\t],\n" +
                "\t\t\"dummy\": \"dummy text\"\n" +
                "\t}"
        File(fullFilename).writeText(fileContent)
        //val file = File(context.filesDir, filename)

        println(fullFilename)
        //ficheiro esta actualmente vazio e nao pode ser editado manualmente
        val tutorial_1: Tutorial = gson.fromJson(FileReader(fullFilename), Tutorial::class.java)
        println("> From JSON Tutorial String:\n" + tutorial_1)

    }

    fun c(){
        /*
        val jsonFileString = getJsonDataFromAsset(context, "bezkoder.json")
        Log.i("data", jsonFileString)

        val gson = Gson()
        val listPersonType = object : TypeToken<List<Person>>() {}.type

        var persons: List<Person> = gson.fromJson(jsonFileString, listPersonType)
        persons.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }*/

    }


}
