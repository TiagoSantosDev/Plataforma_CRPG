package com.plataforma.crpg.ui.reminders

import android.app.Application
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito


class ReminderViewModelTest {

    @Mock @JvmField
    val application: Application? = null

    @Rule
    @JvmField
    var viewModel: ReminderViewModel? = null

    @Before
    fun setUp() {
        viewModel = Mockito.spy(ReminderViewModel(application!!))
    }

    @Test
    fun addReminder() {
    }

}




/*
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Titulo")
            putExtra(AlarmClock.EXTRA_HOUR, 11)
            putExtra(AlarmClock.EXTRA_MINUTES, 30)
            putExtra(AlarmClock.EXTRA_VIBRATE, Boolean.TRUE)
            putExtra(AlarmClock.VALUE_RINGTONE_SILENT, Boolean.TRUE)
            //assertEquals(intent, setAlarmVibrateOnly())
        }
*/


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