package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.plataforma.crpg.model.Event
import com.plataforma.crpg.model.Meal
import com.plataforma.crpg.utils.CustomDateUtils
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

@SuppressLint("StaticFieldLeak")
class MealsViewModel(application: Application) : AndroidViewModel(application) {

    private val m = Meal("01042021", "a", "b", "c", "d")
    var selectedOption = 0
    var retrievedMeal = Meal("27052021", "Lasanha", "Sardinhas", "Massa", "Tofu")
    private val context = getApplication<Application>().applicationContext
    val myLocale = Locale("pt_PT", "POR")


    fun testDB() {
        /*
        val repo = MealsRepository()

        println(">Entrou em testDB")

        repo.initializeDbRef()
        repo.writeNewMealWithTaskListeners(m.data,m.carne,m.peixe,m.dieta,m.vegetariano)
        repo.addPostEventListener()
        */
    }


    fun testJSON() {

        val gson = Gson()
        val json = gson.toJson(m)

        println(json)

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val prettyJson: String = gsonPretty.toJson(json)

        println(prettyJson)

        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        file.writeText(prettyJson)
    }

    fun getMealsFromJSON() {

        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
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

        //println("escreveu no ficheiro")

        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }

    }

    fun fetchMealChoiceOnLocalStorage(): String {

        var selOption = 0
        var dish = ""
        val isLunch = CustomDateUtils.getIsLunchOrDinner()
        val currentDate = CustomDateUtils.getCurrentDay()

        println("isLunch: $isLunch")
        println("Current date: $currentDate")

        selOption = verifyMealChoiceOnLocalStorage(currentDate, isLunch)

        dish = when(selOption){
            1 -> retrievedMeal.carne
            2 -> retrievedMeal.peixe
            3 -> retrievedMeal.dieta
            4 -> retrievedMeal.vegetariano
            else -> ""
        }

        println("Retrieved dish:$dish")

        return dish

    }

    fun updateMealChoiceOnLocalStorage(selectedDate: String, selectedOption: Int, isLunch: Boolean) {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val eventsList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)

        when (isLunch) {
            true -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "ALMOÇO"
                }

                eventsList[idx].meal_int = selectedOption
                eventsList[idx].isLunch = true

                when (selectedOption) {
                    1 -> eventsList[idx].chosen_meal = retrievedMeal.carne
                    2 -> eventsList[idx].chosen_meal = retrievedMeal.peixe
                    3 -> eventsList[idx].chosen_meal = retrievedMeal.dieta
                    4 -> eventsList[idx].chosen_meal = retrievedMeal.vegetariano
                }

            }

            false -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "JANTAR"
                }

                eventsList[idx].meal_int = selectedOption
                eventsList[idx].isLunch = false

                when (selectedOption) {
                    1 -> eventsList[idx].chosen_meal = retrievedMeal.carne
                    2 -> eventsList[idx].chosen_meal = retrievedMeal.peixe
                    3 -> eventsList[idx].chosen_meal = retrievedMeal.dieta
                    4 -> eventsList[idx].chosen_meal = retrievedMeal.vegetariano
                }
            }
        }

        val newMealJSON = gson.toJson(eventsList)
        File(fullFilename).writeText(newMealJSON)

        fetchMealChoiceOnLocalStorage()
    }


    private fun verifyMealChoiceOnLocalStorage(selectedDate: String, isLunch: Boolean): Int {
        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val eventsList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)

        println("Events list: $eventsList")

        when (isLunch) {
            true -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "ALMOÇO"
                }
                return if(eventsList[idx].meal_int < 1 || eventsList[idx].meal_int > 4) 0
                    else eventsList[idx].meal_int

            }

            false -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "JANTAR"
                }

                return if(eventsList[idx].meal_int < 1 || eventsList[idx].meal_int > 4) 0
                    else eventsList[idx].meal_int
            }
        }

    }
}




/*
when (selectedOption) {
    1 -> eventsList[idx].chosen_meal = retrievedMeal.carne
    2 -> eventsList[idx].chosen_meal = retrievedMeal.peixe
    3 -> eventsList[idx].chosen_meal = retrievedMeal.dieta
    4 -> eventsList[idx].chosen_meal = retrievedMeal.vegetariano
}*/
/*
when (selectedOption) {
    1 -> eventsList[idx].chosen_meal = retrievedMeal.carne
    2 -> eventsList[idx].chosen_meal = retrievedMeal.peixe
    3 -> eventsList[idx].chosen_meal = retrievedMeal.dieta
    4 -> eventsList[idx].chosen_meal = retrievedMeal.vegetariano
}*/
//println(">isLunch idx:$idx")
//println(">Set chosen Meal: " + eventsList[idx].chosen_meal)
//println(">Set chosen Meal: " + eventsList[idx].chosen_meal)
//println(">isDinner idx:$idx")
//println("Events list debug: $eventsList")
//println(File(fullFilename).canRead())
//println(File(fullFilename).readText())

// val gsonPretty = GsonBuilder().setPrettyPrinting().create()
// val jsonTutsListPretty: String = gsonPretty.toJson(json)
// File("testPretty.json").writeText(jsonTutsListPretty)fun testJSON_Extract() {
//
//        // val json = """{"title": "Kotlin Tutorial",
//        //    |"author": "bezkoder", "categories" : ["Kotlin","Basic"]}""".trimMargin()
//
//        val gson = Gson()
//        val filename = "meals.json"
//        val file = File(context.filesDir, filename)
//
//        // val meal_1: Meal = gson.fromJson(json, Tutorial::class.java)
//
//        /*
//        var jsonString: String = ""
//        try {
//            jsonString = file.bufferedReader().use { it.readText() }
//        } catch (ioException: IOException) {
//            ioException.printStackTrace()
//        }
//
//        println(jsonString)*/
//
//        /*
//        val objects: Meal = gson.fromJson(jsonString, Meal::class.java)
//        println("Objects: ")
//        println(objects)
//
//        val mealMap = object : TypeToken<Map<String, Any>>() {}.type
//        try {
//            var tutorialMap: Map<String, Any> = gson.fromJson(jsonString, object :
//                    TypeToken<Map<String, Any>>() {}.type)
//                    tutorialMap.forEach { println(it) }
//                    println(tutorialMap)
//        }catch (e: JsonSyntaxException) {}*/
//    }
//    /*
//        if(eventsList.any { it.date == selectedDate }){
//            indexOf.
//        }*/
//
//
//        /*
//        var isLunch = false
//        val sdf = SimpleDateFormat("ddMMyyyy", myLocale)
//        val currentDate = sdf.format(Date())
//        val sdfh = SimpleDateFormat("HH", myLocale)
//        val currentHour = sdfh.format(Date())
//
//        System.out.println(" C DATE is  $currentDate")
//        System.out.println(" C HOUR is  $currentHour")
//
//        //verificar se esta na hora de mostrar notificacao ao utilizador
//        if (currentHour.toString().toInt() in 8..12){
//            println("entre 8 e 12")
//            selOption = verifyMealChoiceOnLocalStorage(currentDate, true)
//            println("selected option: $selOption")
//            isLunch = true
//        }else if(currentHour.toString().toInt() in 13..20){
//            println("entre 14 e 20")
//            selOption = verifyMealChoiceOnLocalStorage(currentDate, false)
//            println("selected option: $selOption")
//        }
//        */