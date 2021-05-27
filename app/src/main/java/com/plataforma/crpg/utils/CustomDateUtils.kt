package com.plataforma.crpg.utils

import java.text.SimpleDateFormat
import java.util.*

object CustomDateUtils {

    val myLocale = Locale("pt_PT", "POR")

    fun getIsLunchOrDinner(): Boolean {

        var isLunch = false
        val sdf = SimpleDateFormat("ddMMyyyy", myLocale)
        val currentDate = sdf.format(Date())
        val sdfh = SimpleDateFormat("HH", myLocale)
        val currentHour = sdfh.format(Date())
        println(" C DATE is  $currentDate")
        println(" C HOUR is  $currentHour")

        //verificar se esta na hora de mostrar notificacao ao utilizador
        if (currentHour.toString().toInt() in 8..12) {
            println("entre 8 e 12")
            isLunch = true
        } else if (currentHour.toString().toInt() in 13..20) {
            println("entre 14 e 20")
            isLunch = false
        }

        return isLunch
    }

    fun getCurrentDay(): String {
        val sdf = SimpleDateFormat("ddMMyyyy", myLocale)
        return sdf.format(Date())
    }

}