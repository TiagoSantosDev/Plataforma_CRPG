package com.plataforma.crpg.ui.agenda

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_date_picker.*
import kotlinx.android.synthetic.main.reminder_activity.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class DatePickerFragment : Fragment() {

    private var currentMonth = 0
    private var selected = false
    private var ttsFlag = false
    private var firstTimeFlag = false
    private val calendar = Calendar.getInstance()
    private var textToSpeech: TextToSpeech? = null

    val myLocale = Locale("pt_PT", "POR")

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

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

    override fun onPause() {
        super.onPause()

        handler.removeCallbacksAndMessages(null)

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("Shutdown Date Picker SR")
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacksAndMessages(null)

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("Shutdown Date Picker SR")
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

        fragmentManager?.beginTransaction()?.remove(this@DatePickerFragment)?.commitAllowingStateLoss()

    }

    /*
    @Override
    protected fun onSaveInstanceState(outState: Bundle?) {
        //No call for super(). Bug on API Level > 11.
    }*/


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        //ttsDatePickerHint()
        val root = inflater.inflate(R.layout.fragment_date_picker, container, false)
        /*
        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("meditationHasRun", false)

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

                println("Position: $position")
                println("Is selected:$isSelected")

                // if item is selected we return this layout items
                // in this example. monday, wednesday and friday will have special item views and other days
                // will be using basic item view

                /*
                if (isSelected){
                    R.layout.selected_calendar_item
                }else{
                    R.layout.calendar_item
                }*/


                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> { println(">Item selecionado")
                            R.layout.selected_calendar_item}
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            //println(">item nao selecionado")
                            R.layout.calendar_item
                        }
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
                //("Position:$position")
                //println("Date: $date")
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
                    // Calendar.SATURDAY -> false
                    //Calendar.SUNDAY -> false
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
                onPause()
            }else{
                no_date_selected_warning.visibility = VISIBLE
            }
        }

        defineModality(ttsFlag, srFlag, hasRun, singleRowCalendar)
        */
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("meditationHasRun", false)

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

                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            R.layout.selected_calendar_item}
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            //println(">item nao selecionado")
                            R.layout.calendar_item
                        }
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

                println("Position: $position")
                // saturday and sunday are disabled as CRPG is not open on these days
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    // Calendar.SATURDAY -> false
                    //Calendar.SUNDAY -> false
                    else -> true
                }
            }

        }

        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            //initialPositionIndex = 10
            init()
        }

        button_selecionar.setOnClickListener {
            if(selected) {
                no_date_selected_warning.visibility = GONE
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment, "Agenda")
                fragmentTransaction.addToBackStack(null)
                //fragmentTransaction.remove(this@DatePickerFragment)
                fragmentTransaction.commit()
                onDestroy()
            }else{
                no_date_selected_warning.visibility = VISIBLE
            }
        }

        defineModality(ttsFlag, srFlag, hasRun, singleRowCalendar)

    }

    override fun onActivityCreated(savedInstanceType: Bundle?) {
        super.onActivityCreated(savedInstanceType)
        //(activity as AppCompatActivity).supportActionBar?.title = "ESCOLHER DATA"
        /*
        println("On Activity Created")

        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("meditationHasRun", false)

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

                println("Position: $position")
                println("Is selected:$isSelected")

                // if item is selected we return this layout items
                // in this example. monday, wednesday and friday will have special item views and other days
                // will be using basic item view

                /*
                if (isSelected){
                    R.layout.selected_calendar_item
                }else{
                    R.layout.calendar_item
                }*/


                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> { println(">Item selecionado")
                            R.layout.selected_calendar_item}
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            //println(">item nao selecionado")
                            R.layout.calendar_item
                        }
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
                //("Position:$position")
                //println("Date: $date")
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
                   // Calendar.SATURDAY -> false
                    //Calendar.SUNDAY -> false
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
                onPause()
            }else{
                no_date_selected_warning.visibility = VISIBLE
            }
        }

        defineModality(ttsFlag, srFlag, hasRun, singleRowCalendar)
        */
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

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean, singleRowCalendar: SingleRowCalendar) {

        println("Entrou na define Modality")

        //singleRowCalendar.initialPositionIndex = 10

        if (!hasRun){
            when{
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition(singleRowCalendar) }
                ttsFlag && srFlag ->{ multimodalOption(singleRowCalendar) }
            }
        }

        if(hasRun){
            when{
                !ttsFlag && srFlag -> { startVoiceRecognition(singleRowCalendar) }
                ttsFlag && srFlag ->{ startVoiceRecognition(singleRowCalendar) }
            }
        }

    }

    private fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                val speechStatus = textToSpeech!!.speak("Diga o dia", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun multimodalOption(singleRowCalendar: SingleRowCalendar) {
        println("Entrou na multimodal option")
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

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        //println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        //("Encerrou TTS")
                        if(activity != null && isAdded) {
                            startVoiceRecognition(singleRowCalendar)
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak("Diga o dia ", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun performActionWithVoiceCommand(command: String, singleRowCalendar: SingleRowCalendar){

        //singleRowCalendar.initialPositionIndex = 10

        when {
            command.contains("Selecionar", true) -> button_selecionar.performClick()
            (command.contains("um", true) || command.contentEquals("1")) -> {singleRowCalendar.clearSelection()
                singleRowCalendar.select(0) }
            (command.contains("dois", true) || command.contentEquals("2")) -> {singleRowCalendar.clearSelection()
                    singleRowCalendar.select(1) }
            (command.contains("três", true) || command.contentEquals("3"))->  {singleRowCalendar.clearSelection()
                    singleRowCalendar.select(2) }
            (command.contains("quatro", true)|| command.contentEquals("4")) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.scrollToPosition(4)
                singleRowCalendar.select(3) }
            (command.contains("cinco", true)|| command.contentEquals("5")) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(4)
                singleRowCalendar.scrollToPosition(5)}
            (command.contains("seis", true)|| command.contentEquals("6")) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(7)
                singleRowCalendar.scrollToPosition(7)}
            (command.contains("sete", true)|| command.contentEquals("7")) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(7)
                singleRowCalendar.scrollToPosition(7)}
            (command.contains("oito", true)|| command.contentEquals("8")) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(7) }
            (command.contains("nove", true)|| command.contentEquals("9")) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(8)
                singleRowCalendar.scrollToPosition(9)}
            (command.contains("dez", true)|| command.contains("10", true)) ->  { singleRowCalendar.clearSelection()
                singleRowCalendar.select(9)
                singleRowCalendar.scrollToPosition(10)}
            (command.contains("onze", true)|| command.contains("11", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(10)
                singleRowCalendar.scrollToPosition(12) }
            (command.contains("doze", true)|| command.contains("12", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(11)
                singleRowCalendar.scrollToPosition(12)}
            (command.contains("treze", true)|| command.contains("13", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(12)
                singleRowCalendar.scrollToPosition(14)}
            (command.contains("catorze", true)|| command.contains("14", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(13)
                singleRowCalendar.scrollToPosition(14)}
            (command.contains("quinze", true)|| command.contains("15", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(14)
                singleRowCalendar.scrollToPosition(16)}
            (command.contains("dezasseis", true)|| command.contains("16", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(15)
                singleRowCalendar.scrollToPosition(16)}
            (command.contains("dezassete", true)|| command.contains("17", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(16)
                singleRowCalendar.scrollToPosition(18)}
            (command.contains("dezoito", true)|| command.contains("18", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(17)
                singleRowCalendar.scrollToPosition(18)}
            (command.contains("dezanove", true)|| command.contains("19", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(18)
                singleRowCalendar.scrollToPosition(20)}
            (command.contains("vinte", true)|| command.contains("20", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(19)
                singleRowCalendar.scrollToPosition(20)}
            (command.contains("vinte e um", true)|| command.contains("21", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(20)
                singleRowCalendar.scrollToPosition(22)}
            (command.contains("vinte e dois", true)|| command.contains("22", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(21)
                singleRowCalendar.scrollToPosition(22)}
            (command.contains("vinte e três", true)|| command.contains("23", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(22)
                singleRowCalendar.scrollToPosition(24)}
            (command.contains("vinte e quatro", true)|| command.contains("24", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(23)
                singleRowCalendar.scrollToPosition(24)}
            (command.contains("vinte e cinco", true)|| command.contains("25", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(24)
                singleRowCalendar.scrollToPosition(26) }
            (command.contains("vinte e seis", true)|| command.contains("26", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(25)
                singleRowCalendar.scrollToPosition(26)}
            (command.contains("vinte e sete", true)|| command.contains("27", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(26)
                singleRowCalendar.scrollToPosition(28)}
            (command.contains("vinte e oito", true)|| command.contains("28", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(27)
                singleRowCalendar.scrollToPosition(28)}
            (command.contains("vinte e nove", true)|| command.contains("29", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(28)
                singleRowCalendar.scrollToPosition(30)}
            (command.contains("trinta", true)|| command.contains("30", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(29)
                singleRowCalendar.scrollToPosition(30)}
            (command.contains("trinta e um", true)|| command.contains("3q", true)) ->  {singleRowCalendar.clearSelection()
                singleRowCalendar.select(30)
                singleRowCalendar.scrollToPosition(30)}
        }
    }

    fun startVoiceRecognition(singleRowCalendar: SingleRowCalendar){
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        println("Entrou na SR")

        if(isAdded && isVisible && getUserVisibleHint()) {
            runnable = Runnable {
                handler.sendEmptyMessage(0);
                Speech.init(requireActivity())
                //hasInitSR = true
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "date picker speech recognition is now active")
                        }

                        override fun onSpeechRmsChanged(value: Float) {
                            //Log.d("speech", "rms is now: $value")
                        }

                        override fun onSpeechPartialResults(results: List<String>) {
                            val str = StringBuilder()
                            for (res in results) {
                                str.append(res).append(" ")
                            }
                            performActionWithVoiceCommand(results.toString(), singleRowCalendar)
                            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                        }

                        override fun onSpeechResult(result: String) {
                            Log.d(TimelineView.TAG, "onSpeechResult: " + result.toLowerCase())
                            //Speech.getInstance().stopTextToSpeech()
                            val handler = Handler()
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if(isAdded && isVisible && getUserVisibleHint()) {
                                            Speech.init(requireActivity())

                                            //hasInitSR = true
                                            Speech.getInstance().startListening(this)
                                        }
                                    } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                        speechRecognitionNotAvailable.printStackTrace()
                                    } catch (e: GoogleVoiceTypingDisabledException) {
                                        e.printStackTrace()
                                    }
                                }, 100)
                            }
                        }
                    })
                } catch (exc: SpeechRecognitionNotAvailable) {
                    Log.e("speech", "Speech recognition is not available on this device!")
                } catch (exc: GoogleVoiceTypingDisabledException) {
                    Log.e("speech", "Google voice typing must be enabled!")
                }
            }

            handler.post(runnable)

        }


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



//singleRowCalendar.calendarChangesObserver.whenCalendarScrolled(50,50)
//println("Position: $position")
//println("Is selected:$isSelected")

// if item is selected we return this layout items
// in this example. monday, wednesday and friday will have special item views and other days
// will be using basic item view

/*
if (isSelected){
    R.layout.selected_calendar_item
}else{
    R.layout.calendar_item
}*/
//("Position:$position")
//println("Date: $date")
//println(">Item selecionado")
/*
if (command.contains("10")) {
    singleRowCalendar.select(9)
    println("Contains True")
}*/

//singleRowCalendar.select(9)

/*if(singleRowCalendar.select(9)){
    println("Conseguiu selecionar, deu true")
}*/

//singleRowCalendar.setItemsSelected(listOf(9), true)

//println("Entrou aqui")
//println("Comparacao:$command" == "10")
//singleRowCalendar.select(9)
//singleRowCalendar.select(10)
/*
        if (command == "10") {
            println("equals True")
        }*/
//singleRowCalendar.setItemsSelected(listOf(5),true)
//println("Int: " + command.toInt().toString())
//singleRowCalendar.select(command.toInt() - 1)
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