package com.plataforma.crpg.ui.reminders

import android.content.Intent
import android.provider.AlarmClock
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Boolean
import java.util.*


class ReminderViewModelTest {

    @Mock
    val fullWeekAlarm = ArrayList<Int>()
    @Mock
    val customWeekAlarm = ArrayList<Int>()

    @Before
    fun setUp() {

        /*
        fullWeekAlarm.add(Calendar.SUNDAY)
        fullWeekAlarm.add(Calendar.MONDAY)
        fullWeekAlarm.add(Calendar.TUESDAY)
        fullWeekAlarm.add(Calendar.WEDNESDAY)
        fullWeekAlarm.add(Calendar.THURSDAY)
        fullWeekAlarm.add(Calendar.FRIDAY)
        fullWeekAlarm.add(Calendar.SATURDAY)

        customWeekAlarm.add(1)
*/
    }

    @Test
    fun addReminder() {
        assertEquals(addReminder(), true)
    }

    @Test
    fun setAlarmVibrateOnly() {

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Titulo")
            putExtra(AlarmClock.EXTRA_HOUR, 11)
            putExtra(AlarmClock.EXTRA_MINUTES, 30)
            putExtra(AlarmClock.EXTRA_VIBRATE, Boolean.TRUE)
            putExtra(AlarmClock.VALUE_RINGTONE_SILENT, Boolean.TRUE)

            //assertEquals(intent, setAlarmVibrateOnly())

        }

        @Test
        fun setAlarmBoth() {
        }

        @Test
        fun setAlarmSoundOnly() {
        }
    }

}