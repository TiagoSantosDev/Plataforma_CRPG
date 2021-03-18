package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plataforma.crpg.model.Meal
import java.io.File
import java.io.FileOutputStream

@SuppressLint("StaticFieldLeak")
class MealDataViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    val m = Meal("a", "b", "c", "d")

    fun testJSON() {

        val gson = Gson()
        val json = gson.toJson(m)

        println(json)

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val prettyJson: String  = gsonPretty.toJson(json)

        println(prettyJson)

        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        file.writeText(prettyJson)
    }


    fun testJSON_Extract() {

        //val json = """{"title": "Kotlin Tutorial",
        //    |"author": "bezkoder", "categories" : ["Kotlin","Basic"]}""".trimMargin()


        val gson = Gson()
        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        //val meal_1: Meal = gson.fromJson(json, Tutorial::class.java)

        /*
        var jsonString: String = ""
        try {
            jsonString = file.bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        println(jsonString)*/





















        /*
        val objects: Meal = gson.fromJson(jsonString, Meal::class.java)
        println("Objects: ")
        println(objects)

        val mealMap = object : TypeToken<Map<String, Any>>() {}.type
        try {
            var tutorialMap: Map<String, Any> = gson.fromJson(jsonString, object :
                    TypeToken<Map<String, Any>>() {}.type)
                    tutorialMap.forEach { println(it) }
                    println(tutorialMap)
        }catch (e: JsonSyntaxException) {}*/

    }

    fun getMealsFromJSON() {

        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
            println(lines)
        }

    }

    fun convertMealsToJSON() {

        val gson = Gson()
        val json = gson.toJson(m)
        val filename = "meals.json"
        val fileContents = "Hello world!"

        val file = File(context.filesDir, filename)

        val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        fos.write(fileContents.toByteArray())

        println("escreveu no ficheiro")

        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
            println(lines)
        }

        println("leu o ficheiro")


        //val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        //val jsonTutsListPretty: String = gsonPretty.toJson(json)
        //File("testPretty.json").writeText(jsonTutsListPretty)
    }


}
