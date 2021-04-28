package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plataforma.crpg.model.*
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    var publicEventList = ArrayList<Event>()
    var privateEventList = ArrayList<Event>()
    var mDataList = ArrayList<Event>()
    var mealsAdded = false

    //create 2 fixed events for lunch and dinner
    var lunchEvent = Event("Almoço", "Clicar para escolher refeição", EventType.MEAL, "1200", "1300",
            "","","")
    var dinnerEvent = Event("Jantar", "Clicar para escolher refeição", EventType.MEAL, "2000", "2100",
            "","","")


    private fun populateFile() {

        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename
        //val fileContent = """[{"title": "sessao","info":"test","type": "ACTIVITY", "start_time": "1130","end_time": "1230","date": "2021-03-17"},{"title": "sessao","info":"test","type":"MEAL", "start_time": "0930","end_time": "1330","date": "2021-03-17"},{"title": "actividade_2","info":"test","type": "TRANSPORT", "start_time": "0830","end_time": "1330","date": "2021-03-17"}]"""
        val fileContent = """[{"title": "ALMOÇO","info":"test","type": "MEAL", "start_time": "1130","end_time": "1230","date": "2021-03-17"},{"title": "JANTAR","info":"test","type":"MEAL", "start_time": "2000","end_time": "2100","date": "2021-03-17"},{"title": "TRANSPORTE","info":"test","type": "TRANSPORT", "start_time": "0830","end_time": "1330","date": "2021-03-17"},{"title": "Actividade","info":"Sala 12","type": "ACTIVITY", "start_time": "0830","end_time": "1330","date": "2021-03-17"}]"""

        File(fullFilename).writeText(fileContent)
    }

    fun getEventCollectionFromJSON(): ArrayList<Event> {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        populateFile()

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val privateList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)

        //println("> From JSON Meal String Event Collection:\n" + privateList)
        //println("Primeira privateList size: " + privateList.size)

        //addMealsToPrivateEvents()
        //verifyMealsNotAdded()
        concatenatePublicPrivateEvents()

        return privateList
    }

    fun getEventCollectionFromJSONWithoutPopulate(): ArrayList<Event> {
        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename
        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val privateList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)
        println("Private List from Without Populate: $privateList")
        return privateList
    }

    fun addMealsToPrivateEvents(): ArrayList<Event> {
        println("Size do privateList a entrada do addMeals:" + privateEventList.size)
        privateEventList.add(lunchEvent)
        privateEventList.add(dinnerEvent)
        return privateEventList
    }

    fun addMealsToPublicEvents(): ArrayList<Event> {
        println("Size do privateList a entrada do addMeals:" + privateEventList.size)
        publicEventList.add(lunchEvent)
        publicEventList.add(dinnerEvent)
        return publicEventList
    }

    fun concatenatePublicPrivateEvents(): ArrayList<Event> {
        addMealsToPrivateEvents()
        addMealsToPublicEvents()
        println(">Private list size: " + privateEventList.size)
        println("Public list size " + publicEventList.size)
        mDataList.plusAssign((privateEventList + publicEventList) as ArrayList<Event>)
        println("Size mDataList: " + mDataList.size)
        return mDataList
    }

}

/*
    fun getSingleObjectFromJSON(): ArrayList<Event> {

        val gson = Gson()
        val filename = "event.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        populateFile()

        val event: Event = gson.fromJson(FileReader(fullFilename), Event::class.java)
        println("> From JSON Meal String Single Object:\n" + event)

        privateEventList.add(event)
        return privateEventList
    }
*/