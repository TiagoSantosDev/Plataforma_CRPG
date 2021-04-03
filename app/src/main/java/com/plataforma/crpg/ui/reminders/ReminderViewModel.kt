package com.plataforma.crpg.ui.reminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.plataforma.crpg.model.AlarmFrequency
import com.plataforma.crpg.model.AlarmType
import com.plataforma.crpg.model.EventModel
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.sql.DriverManager.println
import java.text.SimpleDateFormat
import java.util.*

//Reminders sao eventos criados pelo utilizador e que so sao acedidos pelo proprio
public class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    public val context = application.applicationContext
    // a lista de Reminders contem todos os reminders criados pelo utilizador que ainda vao acontecer no futuro
    var mReminderList = ArrayList<EventModel>()
    //for each day of the week, a boolean indicates if the alarm is set for that day
    val weekDaysBoolean: BooleanArray = booleanArrayOf(false, false, false,
            false, false, false, false)
    var newReminder = EventModel("", "", "", "", "",
            "", AlarmType.SOM, AlarmFrequency.HOJE)
    var startTimeHours : String = ""
    var startTimeMin: String = ""
    private var reminder: MutableLiveData<String>? = null
    lateinit var alarmIntent: Intent;

    fun retrieveListReminders(){
    }

    //garantir que o lembrete e associado a data certa -> check
    //garantir que os alarmes no telemovel sao ativados com a frequencia pretendida -> falta ativar alarme para dia seguinte
    //garantir que o tipo de alarme e adequado ao que foi pedido pelo utilizador -> check

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun addReminder(){
        
        val fullWeekAlarm = ArrayList<Int>()
        fullWeekAlarm.add(Calendar.SUNDAY)
        fullWeekAlarm.add(Calendar.MONDAY)
        fullWeekAlarm.add(Calendar.TUESDAY)
        fullWeekAlarm.add(Calendar.WEDNESDAY)
        fullWeekAlarm.add(Calendar.THURSDAY)
        fullWeekAlarm.add(Calendar.FRIDAY)
        fullWeekAlarm.add(Calendar.SATURDAY)


        var customWeekAlarmMutable = mutableListOf<Int>()

        for ((idx, value) in weekDaysBoolean.withIndex()) {
            if (value) {
                when (idx) {
                    0 -> customWeekAlarmMutable.add(Calendar.MONDAY)
                    1 -> customWeekAlarmMutable.add(Calendar.TUESDAY)
                    2 -> customWeekAlarmMutable.add(Calendar.WEDNESDAY)
                    3 -> customWeekAlarmMutable.add(Calendar.THURSDAY)
                    4 -> customWeekAlarmMutable.add(Calendar.FRIDAY)
                    5 -> customWeekAlarmMutable.add(Calendar.SATURDAY)
                    6 -> customWeekAlarmMutable.add(Calendar.SUNDAY)
                }
            }
        }

        val customWeekAlarm = customWeekAlarmMutable.toCollection(ArrayList<Int>())
        for (i in customWeekAlarm) println("> i value: $i")

        //data do dia de hoje
        val formatDDMMYYYY = SimpleDateFormat("ddMMyyyy")
        val date = Calendar.getInstance().time
        val formattedDateToday = formatDDMMYYYY.format(date)

        val c = Calendar.getInstance()
        c.add(Calendar.DATE, 1)
        val formattedDateTomorrow = formatDDMMYYYY.format(c.time)

        setDateOnReminder(formattedDateToday, formattedDateTomorrow)

        when(newReminder.alarm_type){
            AlarmType.SOM -> setAlarmSoundOnly(fullWeekAlarm, customWeekAlarm)
            AlarmType.VIBRAR -> setAlarmVibrateOnly(fullWeekAlarm, customWeekAlarm)
            AlarmType.AMBOS -> setAlarmBoth(fullWeekAlarm, customWeekAlarm)
        }

        mReminderList.add(newReminder)
    }

    private fun setDateOnReminder(formattedDateToday: String, formattedDateTomorrow: String){
        when(newReminder.alarm_freq){
            AlarmFrequency.HOJE -> newReminder.date = formattedDateToday.toString()
            AlarmFrequency.AMANHA -> newReminder.date = formattedDateTomorrow.toString()
            AlarmFrequency.TODOS_OS_DIAS -> newReminder.date = "x"
            AlarmFrequency.PERSONALIZADO -> newReminder.date = "custom"
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public fun setAlarmVibrateOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {

        when (newReminder.alarm_freq) {
            //so vibracao
            AlarmFrequency.HOJE -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
            }
            AlarmFrequency.AMANHA -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                //putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                //putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                //AlarmClock nao permite definir alarme para dia especifico
                //Tentar ver se e possivel usando Intents
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
            }
            AlarmFrequency.PERSONALIZADO -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
            }
            else -> println("Não entrou em nenhuma das opcoes")
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public fun setAlarmBoth(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        lateinit var alarmIntent: Intent;

        println("> Entrou em setAlarmBoth")

        when (newReminder.alarm_freq) {
            //c/ vibracao e som
            AlarmFrequency.HOJE -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
            }
            AlarmFrequency.AMANHA -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                //putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                //putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
            }
            AlarmFrequency.PERSONALIZADO -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public fun setAlarmSoundOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        lateinit var alarmIntent: Intent;

        when (newReminder.alarm_freq) {
            //sem vibracao
            AlarmFrequency.HOJE -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE); println("startTimeHours: $startTimeHours, startTimeMins:$startTimeMin")
            }
            AlarmFrequency.AMANHA -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
                putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
            }
            AlarmFrequency.PERSONALIZADO -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
                putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
            }

        }
    }
}
