package com.plataforma.crpg.ui.agenda

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.plataforma.crpg.R
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_date_picker.*
import java.util.*


class DatePickerFragment : Fragment() {

    private var textToSpeech: TextToSpeech? = null
    val myLocale = Locale("pt_PT", "POR")
    private var ttsFlag = false
    private var firstTimeFlag = false
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var selected = false

    companion object {
        var dLocale: Locale? = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val locale = Locale("pt")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config,
                context.resources.displayMetrics)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "ESCOLHER DATA"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        ttsDatePickerHint()
        val root = inflater.inflate(R.layout.fragment_date_picker, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceType: Bundle?) {
        super.onActivityCreated(savedInstanceType)
        //(activity as AppCompatActivity).supportActionBar?.title = "ESCOLHER DATA"
        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        calendar.time = Date()

        // calendar view manager is responsible for our displaying logic
        val myCalendarViewManager = object :
                CalendarViewManager {
            override fun setCalendarViewResourceId(
                    position: Int,
                    date: Date,
                    isSelected: Boolean,
            ): Int {
                // set date to calendar according to position where we are
                val cal = Calendar.getInstance()
                cal.time = date
                if (!isSelected) tvDate.text = getString(R.string.nenhum_dia_selecionado_msg)
                // if item is selected we return this layout items
                // in this example. monday, wednesday and friday will have special item views and other days
                // will be using basic item view
                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.selected_calendar_item
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.calendar_item
                    }

                // NOTE: if we don't want to do it this way, we can simply change color of background
                // in bindDataToCalendarView method
            }

            override fun bindDataToCalendarView(
                    holder: SingleRowCalendarAdapter.CalendarViewHolder,
                    date: Date,
                    position: Int,
                    isSelected: Boolean,
            ) {
                // using this method we can bind data to calendar view
                // good practice is if all views in layout have same IDs in all item views
                holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar_item.text = DateUtils.getDay3LettersName(date)
            }
        }


        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object :
                CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                tvDate.text = "${DateUtils.getDayName(date).capitalize()}, ${DateUtils.getDayNumber(date)} de ${DateUtils.getMonthName(date).capitalize()}"
                tvDay.text = DateUtils.getDayName(date)
                sharedViewModel.selectedDate = DateUtils.getDayNumber(date) + DateUtils.getMonthNumber(date) + DateUtils.getYear(date)
                //println("Selected date: " + sharedViewModel.selectedDate)
                super.whenSelectionChanged(isSelected, position, date)
                selected = isSelected
            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date
                // saturday and sunday are disabled as CRPG is not open on these days
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.SATURDAY -> false
                    Calendar.SUNDAY -> false
                    else -> true
                }
            }

        }

        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        button_selecionar.setOnClickListener {
            if(selected) {
                no_date_selected_warning.visibility = GONE
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment,"Agenda")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }else{
                no_date_selected_warning.visibility = VISIBLE
            }
        }
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    fun ttsDatePickerHint() {

        if (!firstTimeFlag) {
            textToSpeech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val ttsLang = textToSpeech!!.setLanguage(myLocale)
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!")
                    } else {
                        Log.i("TTS", "Language Supported.")
                    }
                    Log.i("TTS", "Initialization success.")

                    if (textToSpeech!!.isSpeaking) {
                        ttsFlag = true
                    }

                    if (!textToSpeech!!.isSpeaking) {
                        ttsFlag = false
                    }

                    //val speechStatus = textToSpeech!!.speak("Por favor selecione um dia movendo os quadrados amarelos para a esquerda e direita e premindo aquele que pretender selecionar", TextToSpeech.QUEUE_FLUSH, null)
                    firstTimeFlag = true
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}





/*
    init {
        //activity?.let { updateConfig(it) }
        //updateConfig(requireActivity().baseContext)
    }
*/
/*
     fun updateConfig(wrapper: ContextThemeWrapper) {
        dLocale = Locale("pt")
        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
*/
/*
    fun dontShowBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }
*/


// set current date to calendar and current month to currentMonth variable
/*var change = ""
val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
val language = sharedPreferences.getString("language", "bak")
if (language == "English") {
    change = "pt"
} else {
    change = "pt"
}
//dontShowBackButton()
//(activity as AppCompatActivity).supportActionBar?.title = "ESCOLHER DATA"
dLocale = Locale(change)*/
//var BottomNavigationView navBar = require.findviewById(R.id.nav_view)
//(activity as AppCompatActivity).supportActionBar?.title = "ESCOLHER DATA"