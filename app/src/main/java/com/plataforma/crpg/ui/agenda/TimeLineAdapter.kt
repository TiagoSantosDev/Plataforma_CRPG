package com.plataforma.crpg.ui.agenda

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.extentions.formatDateTime
import com.plataforma.crpg.extentions.setGone
import com.plataforma.crpg.extentions.setVisible
import com.plataforma.crpg.model.Event
import com.plataforma.crpg.model.EventType
import com.plataforma.crpg.model.TimelineAttributes
import com.plataforma.crpg.ui.meals.MealsFragment
import com.plataforma.crpg.ui.transports.TransportsSelectionFragment
import kotlinx.android.synthetic.main.item_timeline.view.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineAdapter(private val mFeedList: List<Event>, private var mAttributes: TimelineAttributes, private val ctx: Context) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {


    private var overlapArray = mutableListOf("")
    private var concatTime = ""

    private var textToSpeech: TextToSpeech? = null
    private var onResumeFlag = false
    private val myLocale = Locale("pt_PT", "POR")
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    private lateinit var mLayoutInflater: LayoutInflater
    //val Context mContext = getActivity();

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        val view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false)



        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]
        //holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker_active), mAttributes.markerColor)

        //holder.bind(timeLineModel)
        if (timeLineModel.type == EventType.ACTIVITY) {
            holder.itemView.contentDescription = "Actividade" +
                    "com o título ${timeLineModel.title} e tendo como descrição ${timeLineModel.info}, que irá" +
                    " começar às ${timeLineModel.start_time}. Clique para obter mais informações"
        }

        if (timeLineModel.type == EventType.MEAL && timeLineModel.chosen_meal.isNullOrBlank()) {
            holder.itemView.contentDescription = "Nenhuma refeição selecionada, clique para selecionar" +
                     "a sua refeição"
        }

        if (timeLineModel.type == EventType.MEAL && !timeLineModel.chosen_meal.isNullOrBlank()) {
            holder.itemView.contentDescription = "Refeição selecionada, o prato escolhido foi ${timeLineModel.chosen_meal}"
        }

        if (timeLineModel.type == EventType.TRANSPORTS) {
            holder.itemView.contentDescription = "Aceder à secção de transportes"
        }

        concatTime = timeLineModel.start_time + timeLineModel.end_time

        if (overlapArray.contains(concatTime)){
            holder.timeline.marker.setVisible(false, false)
        }else{
            overlapArray.add(concatTime)
            holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker_active), mAttributes.markerColor)
        }

        if (timeLineModel.date.isNotEmpty()) {
            holder.date.setVisible()
            // holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy")
            holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd", "dd-MMM-yyyy")
        } else
            holder.date.setGone()

        if (timeLineModel.start_time.isNotEmpty()) {
            holder.start_time.setVisible()
            var newStartTime: String = timeLineModel.start_time
            newStartTime = newStartTime.substring(0, 2) + ":" + newStartTime.substring(2, 4)
            holder.start_time.text = newStartTime
        } else
            holder.start_time.setGone()

        if (timeLineModel.end_time.isNotEmpty()) {
            holder.end_time.setVisible()
            var newEndTime: String = timeLineModel.end_time
            newEndTime = newEndTime.substring(0, 2) + ":" + newEndTime.substring(2, 4)
            holder.end_time.text = newEndTime
        } else
            holder.start_time.setGone()

        if (timeLineModel.title.isNotEmpty()){
            holder.title.text = timeLineModel.title
        }

        if (timeLineModel.info.isNotEmpty()){
            holder.info.text = timeLineModel.info
        }

        when(timeLineModel.type){
            EventType.ACTIVITY -> {
                holder.itemView.card_background_image.setBackgroundResource(R.drawable.crpg_background)
                holder.itemView.card_center_icon.setBackgroundResource(R.drawable.maos)
                holder.itemView.text_timeline_title.text = "ACTIVIDADE"
                holder.itemView.text_timeline_info.text = timeLineModel.info.toUpperCase()
            }
            EventType.MEAL -> {
                holder.itemView.card_background_image.setBackgroundResource(R.drawable.background_dieta)
                holder.itemView.card_center_icon.setBackgroundResource(R.drawable.meal_icon)

                when (timeLineModel.title) {
                    "ALMOÇO" -> if (timeLineModel.chosen_meal.isNullOrBlank()) {
                        holder.itemView.text_timeline_info.text = "CLIQUE AQUI PARA SELECIONAR ALMOÇO"
                    } else {
                        holder.itemView.text_timeline_info.text = timeLineModel.chosen_meal
                    }

                    "JANTAR" -> if (timeLineModel.chosen_meal.isNullOrBlank()) {
                        holder.itemView.text_timeline_info.text = "CLIQUE AQUI PARA SELECIONAR JANTAR"
                    } else {
                        holder.itemView.text_timeline_info.text = timeLineModel.chosen_meal
                    }

                }

            }
            EventType.TRANSPORTS -> {
                holder.itemView.text_timeline_info.text = "CLIQUE AQUI PARA MAIS INFORMAÇÕES"
                holder.itemView.card_background_image.setBackgroundResource(R.drawable.stcp_background)
                holder.itemView.card_center_icon.setBackgroundResource(R.drawable.bus_icon)
            }
        }

        var id: String
        var tipo: EventType

        //onClick on a card open pop up or go to Meal or Transport Fragment
        holder.itemView.card.setOnClickListener {
            id=mFeedList[position].title
            tipo= mFeedList[position].type

            when(tipo){
                EventType.ACTIVITY -> {
                    MaterialAlertDialogBuilder(ctx,android.R.style.Theme_Material_Dialog_Alert)
                            .setTitle(mFeedList[position].title)
                            .setMessage(mFeedList[position].info)
                            .setNegativeButton("Fechar"){ dialog, which -> }.show()
                }
                EventType.TRANSPORTS -> {
                    val fragment: Fragment = TransportsSelectionFragment()
                    val fragmentManager: FragmentManager = (ctx as AppCompatActivity).supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

                EventType.MEAL -> {
                    val bundle = Bundle()

                    when (id) {
                        "ALMOÇO" -> {
                            println(">title is lunch")
                            bundle.putBoolean("isLunch", true)
                        }
                        "JANTAR" -> {
                            println(">title is dinner")
                            bundle.putBoolean("isLunch", false)
                        }
                    }

                    val fragment: Fragment = MealsFragment()
                    fragment.arguments = bundle
                    val fragmentManager: FragmentManager = (ctx as AppCompatActivity).supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentManager.findFragmentByTag("Agenda")?.let { it1 -> fragmentTransaction.remove(it1) }
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

            }

            fun performActionWithVoiceCommand(command: String) {
                when {
                    command.contains("Escolher Almoço", true)  && id=="Almoço" -> holder.itemView.card.performClick()
                    command.contains("Escolher Jantar", true)  && id=="Jantar"-> holder.itemView.card.performClick()
                    command.contains(id, true) && tipo == EventType.ACTIVITY-> holder.itemView.card.performClick()
                }
            }


            fun multimodalOption() {
                println("First Time flag: $onResumeFlag")
                if (!onResumeFlag) {
                    textToSpeech = TextToSpeech(ctx) { status ->
                        if (status == TextToSpeech.SUCCESS) {
                            val ttsLang = textToSpeech!!.setLanguage(myLocale)
                            if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                    || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "The Language is not supported!")
                            } else {
                                Log.i("TTS", "Language Supported.")
                            }
                            Log.i("TTS", "Initialization success.")

                            var handler = Handler(Looper.getMainLooper())
                            //var runable = Runnable {
                            val speechListener = object : UtteranceProgressListener() {
                                @Override
                                override fun onStart(p0: String?) {
                                    println("Iniciou TTS")
                                }

                                override fun onDone(p0: String?) {
                                    println("Encerrou TTS")
                                    //startVoiceRecognition()
                                }

                                override fun onError(p0: String?) {
                                    TODO("Not yet implemented")
                                }
                            }

                            textToSpeech?.setOnUtteranceProgressListener(speechListener)
                            println("Chegou aqui pos utterance listener")

                            val speechStatus = textToSpeech!!.speak("Selecione uma das opções ou diga o estado" +
                                    "em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")

                        } else {
                            Toast.makeText(ctx, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            fun startVoiceRecognition(){
                runnable = Runnable {
                    handler.sendEmptyMessage(0);
                    Speech.init(ctx)
                    try {
                        Speech.getInstance().startListening(object : SpeechDelegate {
                            override fun onStartOfSpeech() {
                                Log.i("speech", "speech recognition is now active")
                            }

                            override fun onSpeechRmsChanged(value: Float) {
                                //Log.d("speech", "rms is now: $value")
                            }

                            override fun onSpeechPartialResults(results: List<String>) {
                                val str = StringBuilder()
                                for (res in results) {
                                    str.append(res).append(" ")
                                }
                                performActionWithVoiceCommand(results.toString())
                                Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                            }

                            override fun onSpeechResult(result: String) {
                                Log.d(TimelineView.TAG, "onSpeechResult: " + result.toLowerCase())
                                //Speech.getInstance().stopTextToSpeech()
                                val handler = Handler()
                                handler.postDelayed({
                                    try {
                                        Speech.init(ctx)
                                        Speech.getInstance().startListening(this)
                                    } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                        speechRecognitionNotAvailable.printStackTrace()
                                    } catch (e: GoogleVoiceTypingDisabledException) {
                                        e.printStackTrace()
                                    }
                                }, 100)

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

            fun onDestroy() {
                if(handler.hasMessages(0)) {
                    handler.removeCallbacks(runnable)
                }

                if (textToSpeech != null) {
                    textToSpeech!!.stop()
                    textToSpeech!!.shutdown()
                    println("shutdown TTS")
                }
            }

        }

    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date = itemView.text_timeline_date
        val title = itemView.text_timeline_title
        val info = itemView.text_timeline_info
        val start_time = itemView.text_timeline_start_time
        val end_time = itemView.text_timeline_end_time
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
            timeline.markerSize = mAttributes.markerSize
            timeline.setMarkerColor(mAttributes.markerColor)
            timeline.isMarkerInCenter = mAttributes.markerInCenter
            timeline.markerPaddingLeft = mAttributes.markerLeftPadding
            timeline.markerPaddingTop = mAttributes.markerTopPadding
            timeline.markerPaddingRight = mAttributes.markerRightPadding
            timeline.markerPaddingBottom = mAttributes.markerBottomPadding
            timeline.linePadding = mAttributes.linePadding

            timeline.lineWidth = mAttributes.lineWidth
            timeline.setStartLineColor(mAttributes.startLineColor, viewType)
            timeline.setEndLineColor(mAttributes.endLineColor, viewType)
            timeline.lineStyle = mAttributes.lineStyle
            timeline.lineStyleDashLength = mAttributes.lineDashWidth
            timeline.lineStyleDashGap = mAttributes.lineDashGap
        }
    }
}


/*if (mAttributes.orientation == Orientation.HORIZONTAL) {
//mLayoutInflater.inflate(R.layout.item_timeline_horizontal, parent, false)
} else {*/
//}
//private var currentDateMealOrder = MealOrder("14042021", 0, pedido_carne = false, pedido_peixe = false, pedido_dieta = false, pedido_vegetariano = false)
//lateinit var currentDateMeal: Meal
//val id: String = mFeedList[position].title
//val tipo: EventType = mFeedList[position].type
//MANTER WIFI SEMPRE LIGADO
// val handler = Handler(Looper.getMainLooper())
// val runable = Runnable {
/*
        fun performActionWithVoiceCommand(command: String) {
            when {
                command.contains("Escolher Almoço", true)  && id=="Almoço" -> holder.itemView.card.performClick()
                command.contains("Escolher Jantar", true)  && id=="Jantar"-> holder.itemView.card.performClick()
                command.contains(id, true) && tipo == EventType.ACTIVITY-> holder.itemView.card.performClick()
            }
        }

        Speech.init(ctx)

        try {
            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    Log.i("speech", "speech recognition is now active")
                }

                override fun onSpeechRmsChanged(value: Float) {
                    Log.d("speech", "rms is now: $value")
                }

                override fun onSpeechPartialResults(results: List<String>) {
                    val str = StringBuilder()
                    for (res in results) {
                        str.append(res).append(" ")
                    }
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    //performActionWithVoiceCommand(result)
                    Log.i("speech", "result: $result")
                    //println("on Speech Result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")

        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }
        */
//println("Valor de chosen meal:" +  timeLineModel.chosen_meal)
//val fragmentManager: FragmentManager? = (context as MainActivity?)?.supportFragmentManager
//val fragmentManager: FragmentManager? = (context as MainActivity?)?.supportFragmentManager
// val fragmentManager: FragmentManager? = (context as MainActivity?)?.supportFragmentManager
// private val contextMeal: MealsFragment.Companion = MealsFragment
////private val contextTransport: TransportsFragment.Companion = TransportsFragment
// val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
// Toast.makeText(context, "Transports", Toast.LENGTH_SHORT).show()
//
// /*
//        holder.itemView.card.setOnClickListener{
//            println("clicou na carta")
//        }*/
//
// println("Fragment Manager: " + fragmentManager.toString())
//                    //println("Fragment Transaction: " + fragmentTransaction.toString())
//
// fragment.arguments(bundle)
// /*val storeDetails = StoreDetails()
//                            storeDetails.setArguments(bundle)
//                            getActivity().getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.content_frame, StoreDetails())
//                                    .commit()*/
// println(">TimeLine Model chosen Meal: " + timeLineModel.chosen_meal)
//
// val title: String =  mFeedList[position].title
//            //println("ID do cartao: $id")
//            //println("Tipo do cartao: $tipo")//se almoço nao estiver escolhido
//        //card informa que pode ser clicado para escolher almoço
//        /*
//        if (timeLineModel.title.isNotEmpty() && timeLineModel.chosen_meal.isNullOrBlank()) {
//            when(timeLineModel.title){
//                "ALMOÇO" -> {
//                    holder.title.text = timeLineModel.title
//                    //holder.info.text = "Clicar para selecionar almoço!"
//                }
//                "JANTAR" -> {
//                    holder.title.text = timeLineModel.title
//                    //holder.info.text = "Clicar para selecionar jantar!"
//                }
//                "TRANSPORTE" -> {
//                    holder.title.text = timeLineModel.title
//                    holder.info.text = "Clicar para horários dos transportes!"
//                }
//                else -> {holder.title.text = timeLineModel.title
//                holder.info.text = timeLineModel.info}
//            }
//        } else if(timeLineModel.title.isNotEmpty() && !timeLineModel.chosen_meal.isNullOrBlank()){
//            when(currentDateMealOrder.pedido_feito){
//                //1-> holder.info.text = currentDateMeal.prato_carne
//                //2-> holder.info.text = currentDateMeal.prato_peixe
//                //3-> holder.info.text = currentDateMeal.prato_dieta
//                //4-> holder.info.text = currentDateMeal.prato_vegetariano
//            }
//        }*/