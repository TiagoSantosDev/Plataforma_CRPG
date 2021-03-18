package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.plataforma.crpg.model.Meal
import com.plataforma.crpg.model.Tutorial
import java.io.File
import java.io.FileReader
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class Test(application: Application) : AndroidViewModel(application) {

    public val context = application.applicationContext
    val mMealList = ArrayList<Meal>()

    fun a() {
        val json = """{"title": "Kotlin Tutorial #1", "author": "bezkoder", "categories" : ["Kotlin","Basic"]}"""
        val jsonMeal = """{"carne": "a", "peixe": "b", "dieta" : "c", "vegetariano": "d"}"""
        val gson = Gson()

        val tutorial_1: Tutorial = gson.fromJson(json, Tutorial::class.java)
        println("> From JSON String:\n" + tutorial_1)

        val tutorial_2: Meal = gson.fromJson(jsonMeal, Meal::class.java)
        println("> From JSON Meal String:\n" + tutorial_2)
    }

    fun b() {

        val gson = Gson()
        val filename = "test.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val fileContent = """{"carne": "a", "peixe": "b", "dieta" : "c", "vegetariano": "d"}"""
        File(fullFilename).writeText(fileContent)
        // val file = File(context.filesDir, filename)

        val meal_1: Meal = gson.fromJson(FileReader(fullFilename), Meal::class.java)
        println("> From JSON Meal String:\n" + meal_1)
    }

    fun c() {
        /*
        val jsonFileString = getJsonDataFromAsset(context, "bezkoder.json")
        Log.i("data", jsonFileString)

        val gson = Gson()
        val listPersonType = object : TypeToken<List<Person>>() {}.type

        var persons: List<Person> = gson.fromJson(jsonFileString, listPersonType)
        persons.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }*/
    }

    fun insertData() {
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
        // val file = File(context.filesDir, filename)

        println(fullFilename)
        // ficheiro esta actualmente vazio e nao pode ser editado manualmente
        val tutorial_1: Tutorial = gson.fromJson(FileReader(fullFilename), Tutorial::class.java)
        println("> From JSON Tutorial String:\n" + tutorial_1)
    }

    fun getSingleMealFromJson(): ArrayList<Meal> {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val fileContent = """{"carne": "a", "peixe": "b", "dieta" : "c", "vegetariano": "d"}"""
        File(fullFilename).writeText(fileContent)
        // val file = File(context.filesDir, filename)

        val event: Meal = gson.fromJson(FileReader(fullFilename), Meal::class.java)
        println("> From JSON Meal String:\n" + event)

        mMealList.add(event)
        return mMealList
    }
}
