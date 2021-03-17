package com.github.vipulasri.timelineview.sample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.vipulasri.timelineview.sample.model.EventModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*


class EventDataViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    val mDataList = ArrayList<EventModel>()
    val mMealList = ArrayList<Meal>()

    fun populateFile() {

        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename
        val fileContent = """[{"title": "sessao","info":"test","start_time": "1130","end_time": "1230","date": "2021-03-17"}]"""

        File(fullFilename).writeText(fileContent)

    }


    fun getSingleObjectFromJSON(): ArrayList<EventModel> {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        populateFile()

        val event: EventModel = gson.fromJson(FileReader(fullFilename), EventModel::class.java)
        println("> From JSON Meal String:\n" + event)

        mDataList.add(event)
        return mDataList
    }

    fun getEventCollectionFromJSON(): ArrayList<EventModel> {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        populateFile()

        val type: Type = object : TypeToken<ArrayList<EventModel>>() {}.type
        val mDataList: ArrayList<EventModel> = gson.fromJson(FileReader(fullFilename), type)

        println("> From JSON Meal String:\n" + mDataList)

        return mDataList
    }

    fun setDataListItems(): ArrayList<EventModel> {

        mDataList.add(
                EventModel(
                        "evento1", "a", "11:00", "12:00",
                        "2017-02-12"
                )
        )
        mDataList.add(
                EventModel(
                        "evento1", "a", "11:00", "12:00",
                        "2017-02-12"
                )
        )
        mDataList.add(
                EventModel(
                        "evento2", "a", "11:00", "12:00",
                        "2017-02-11"
                )
        )

        return mDataList
    }

    fun getMealFromJson(): ArrayList<Meal> {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val fileContent = """{"carne": "a", "peixe": "b", "dieta" : "c", "vegetariano": "d"}"""
        File(fullFilename).writeText(fileContent)
        //val file = File(context.filesDir, filename)

        val event: Meal = gson.fromJson(FileReader(fullFilename), Meal::class.java)
        println("> From JSON Meal String:\n" + event)

        mMealList.add(event)
        return mMealList
    }
}
