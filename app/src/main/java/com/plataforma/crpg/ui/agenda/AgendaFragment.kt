package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.extentions.dpToPx
import com.plataforma.crpg.extentions.getColorCompat
import com.plataforma.crpg.extentions.setGone
import com.plataforma.crpg.extentions.setVisible
import com.plataforma.crpg.model.Event
import com.plataforma.crpg.model.Orientation
import com.plataforma.crpg.model.TimelineAttributes
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_agenda.*
import java.util.*

class AgendaFragment : Fragment() {

    private var textToSpeech: TextToSpeech? = null
    val myLocale = Locale("pt_PT", "POR")
    private var firstTimeFlag = false
    private var ttsFlag = false
    private var mDataList = ArrayList<Event>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "AGENDA"
        (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setDataListItemsWithoutPopulate()
        val ctx = context
        if (ctx != null) {
            initRecyclerView(ctx)
        }

        mAttributes.onOrientationChanged = { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView(ctx!!)
        }
        mAttributes.orientation = Orientation.VERTICAL
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)

        //ttsAgendaHint()
        val root = inflater.inflate(R.layout.fragment_agenda, container, false)
        return root
    }

    override fun onDestroy() {
        super.onDestroy()

        //adapter.onDestroy()

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "AGENDA"
        mAttributes = TimelineAttributes(
                markerSize = dpToPx(20f),
                markerColor = getColorCompat(R.color.material_grey_500),
                markerInCenter = true,
                markerLeftPadding = dpToPx(0f),
                markerTopPadding = dpToPx(0f),
                markerRightPadding = dpToPx(0f),
                markerBottomPadding = dpToPx(0f),
                linePadding = dpToPx(2f),
                startLineColor = getColorCompat(R.color.colorAccent),
                endLineColor = getColorCompat(R.color.colorAccent),
                lineStyle = TimelineView.LineStyle.NORMAL,
                lineWidth = dpToPx(2f),
                lineDashWidth = dpToPx(4f),
                lineDashGap = dpToPx(2f)
        )
        val ctx = context

        //reset aos dados do ficheiro
        //setDataListItems()
        //apenas vai buscar os dados ja existentes
        setDataListItemsWithoutPopulate()
        if (ctx != null) {
            initRecyclerView(ctx)
        }

        mAttributes.onOrientationChanged = { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView(ctx!!)
        }

        mAttributes.orientation = Orientation.VERTICAL
    }

    private fun setDataListItems() {
        val eventViewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)

        mDataList = eventViewModel.getEventCollectionFromJSON()
        // guarantee that all events are sorted by their starting time
        mDataList.sortBy { it.start_time }
    }

    private fun setDataListItemsWithoutPopulate() {
        val eventViewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)

        mDataList = eventViewModel.getEventCollectionFromJSONWithoutPopulate()
        // guarantee that all events are sorted by their starting time
        mDataList.sortBy { it.start_time }

        println(mDataList)
    }

    private fun initRecyclerView(ctx: Context) {
        initAdapter(ctx)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.getChildAt(0).top < 0) dropshadow.setVisible() else dropshadow.setGone()
            }
        })
    }

    private fun initAdapter(ctx: Context) {
        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = TimeLineAdapter(mDataList, mAttributes, ctx)
        }
    }

    fun ttsAgendaHint() {

        println("First Time flag: " + firstTimeFlag)
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

                    val speechStatus = textToSpeech!!.speak("Clique num dos eventos ou diga em voz alta o nome para saber mais informações", TextToSpeech.QUEUE_FLUSH, null)
                    firstTimeFlag = true
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val speechListener = object : UtteranceProgressListener() {
            @Override

            override fun onStart(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onDone(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onError(p0: String?) {
                TODO("Not yet implemented")
            }
        }
        textToSpeech?.setOnUtteranceProgressListener(speechListener)

    }


}


/*
        //val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        //println("Selected date: " + sharedViewModel.selectedDate)

        // default values
//println("Selected date: " + sharedViewModel.selectedDate)
        action_example_activity.setOnClickListener {
            println(">Activity: " + activity)
            val intent = Intent(activity, ExampleActivity::class.java)
            startActivity(intent)
        }*/
/*
        fab_options.setOnClickListener {
            TimelineAttributesBottomSheet.showDialog(
                parentFragmentManager, mAttributes,
                object : TimelineAttributesBottomSheet.Callbacks {
                    override fun onAttributesChanged(attributes: TimelineAttributes) {
                        mAttributes = attributes
                        initAdapter()
                    }
                }
            )
        }
*/
//val ctx = context
//println("CONTEXT IS NULL onCreateView:" + ctx.toString())
// println("CONTEXT IS NULL onViewCreated:" + ctx.toString())
////println("CTX NAO E NULL")
// println("Activity:" + activity)
//        //println("Recycler View:" + recyclerView)
// val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
//        //val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)