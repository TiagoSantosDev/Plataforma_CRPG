/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.plataforma.crpg.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.plataforma.crpg.model.AlarmFrequency
import com.plataforma.crpg.model.AlarmType
import com.plataforma.crpg.model.Reminder
import com.plataforma.crpg.model.ReminderType
import java.util.*
import java.util.Calendar.*

/**
 * Helpers to assist in scheduling alarms for notifications.
 */
object AlarmScheduler {

    fun scheduleAlarmsForReminder(context: Context) {

        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // get the PendingIntent for the alarm
        val alarmIntent = createPendingIntent(context, "Sexta")

        var reminder = Reminder("","","",
                0,0,"", "",ReminderType.REFEICAO,AlarmType.AMBOS,AlarmFrequency.AMANHA)

        scheduleAlarm(1, true, reminder, alarmIntent, alarmMgr)

    }

    fun scheduleAlarmsForAllReminder(context: Context) {

        val reminderList : ArrayList<Reminder>

        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // get the PendingIntent for the alarm
        val alarmIntent = createPendingIntent(context, "Sexta")

/*
        for (i in reminderList){
            scheduleAlarm(1, true, reminderList.get(i), alarmIntent, alarmMgr)
        }
*/

    }

    private fun createPendingIntent(context: Context, day: String?): PendingIntent? {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            type = "$day- teste funcionou"
        }
        val broadcastID = (System.currentTimeMillis() * 2).toInt()
        return PendingIntent.getBroadcast(context, broadcastID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun scheduleAlarm(dayOfWeek: Int, repeats: Boolean, reminder: Reminder,
                                    alarmIntent: PendingIntent?, alarmMgr: AlarmManager) {

        println("Entra no schedule alarm")

        // Set up the time to schedule the alarm
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())

        println("Dia:" + datetimeToAlarm.time.day)
        println("Horas:" + datetimeToAlarm.time.hours)
        println("Minutos:" + datetimeToAlarm.time.minutes)

        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(DAY_OF_WEEK, 6)
        //datetimeToAlarm.set(HOUR_OF_DAY, reminder.start_time.take(2).toInt())
        //datetimeToAlarm.set(MINUTE, reminder.start_time.takeLast(2).toInt())
        //datetimeToAlarm.set(HOUR_OF_DAY, reminder.hours)
        //datetimeToAlarm.set(MINUTE, reminder.mins)
        datetimeToAlarm.set(HOUR_OF_DAY, 15)
        datetimeToAlarm.set(MINUTE, 23)
        datetimeToAlarm.set(SECOND, 0)
        datetimeToAlarm.set(MILLISECOND, 0)

        // Compare the datetimeToAlarm to today
        val today = Calendar.getInstance(Locale.getDefault())

        if (reminder.alarm_freq == AlarmFrequency.TODOS_OS_DIAS) {
            // schedule for every single day starting from today
            alarmMgr.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    datetimeToAlarm.timeInMillis, (1000 * 60 * 60 * 24 * 7).toLong(), alarmIntent
            )
        }else if(reminder.alarm_freq == AlarmFrequency.HOJE){
            //Single time alarm
            alarmMgr.set(AlarmManager.RTC_WAKEUP, datetimeToAlarm.timeInMillis, alarmIntent);
        }
    }


}
//putExtra(ReminderDialog.KEY_ID, reminderData.id)
// action = context.getString(R.string.action_notify_administer_medication)