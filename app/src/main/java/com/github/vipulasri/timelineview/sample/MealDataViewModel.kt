package com.github.vipulasri.timelineview.sample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileOutputStream

@SuppressLint("StaticFieldLeak")
class MealDataViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    val TAG = "JSONParser"

    val m = Meal("a", "b", "c", "d")

    //val a = application.getApplicationContext()

    fun convertMealsToJSON() {

        val gson = Gson()
        val json = gson.toJson(m)
        val filename = "myfile.json"
        val fileContents = "Hello world!"

       /* fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }*/

        val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        fos.write(fileContents.toByteArray())

        // View list of files
        // var files: Array<String> = context.fileList()

        //val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        //val jsonTutsListPretty: String = gsonPretty.toJson(json)
        //File("testPretty.json").writeText(jsonTutsListPretty)
    }

    private fun getMealsFromJSON() {
    }
}
