package com.plataforma.crpg.ui.transports

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.FragmentPublicTransportBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.fragment_custom_transport.button_return_transports
import kotlinx.android.synthetic.main.fragment_public_transport.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class PublicTransportsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()
    private var textToSpeech: TextToSpeech? = null
    private val myLocale = Locale("pt_PT", "POR")

    private var hasInitSR = false
    private var opSelected = 0
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var transportsViewModel: TransportsViewModel

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("publicTransportHasRun", true).apply()
    }

    override fun onDestroy() {
        // Don't forget to shutdown!

        handler.removeCallbacksAndMessages(null)

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

        super.onDestroy ();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("publicTransportsHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment: Fragment = TransportsSelectionFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPublicTransportBinding.inflate(layoutInflater)
        val view = binding.root

        val linesSpinner: Spinner? = view.findViewById(R.id.bus_lines_spinner)
        ArrayAdapter.createFromResource(
                context,
                R.array.linha_autocarro_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            linesSpinner?.adapter = adapter
        }

        linesSpinner?.onItemSelectedListener = this
        return view
        //return inflater.inflate(R.layout.fragment_public_transports_old, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "TRANSPORTES PUBLICOS"

        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        val selectedDate = sharedViewModel.selectedDate
        val publicText = transportsViewModel.getPublicTransportText(selectedDate)
        println("Custom text: $publicText")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_view_timetables_layout.setOnClickListener{
            val fragment: Fragment = PublicTransportsTimetableFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        button_return_transports.setOnClickListener {
            val fragment: Fragment = TransportsSelectionFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PublicTransportsFragment().apply {
                }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val selectedDate = sharedViewModel.selectedDate
        val publicText = transportsViewModel.getPublicTransportText(selectedDate)
        println("Custom text: $publicText")
        println("Item clicked: $p2")

        when(p2){
            0 -> public_transports_text.text = publicText["Linha 905"]
            1 -> public_transports_text.text = publicText["Linha ZF"]
            2 -> public_transports_text.text = publicText["Linha 35"]
            3 -> public_transports_text.text = publicText["Linha 45"]
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("901", true) || command.contains("novecentos e um", true) -> bus_lines_spinner.setSelection(0)
            command.contains("ZF", true) -> bus_lines_spinner.setSelection(1)
            command.contains("35", true)  || command.contains("trinta e cinco", true) -> bus_lines_spinner.setSelection(2)
            command.contains("45", true) || command.contains("quarenta e cinco", true) -> bus_lines_spinner.setSelection(3)
            command.contains("consultar horários", true) -> button_view_timetables_layout.performClick()
        }
    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {

        println("ttsFlag:  " + ttsFlag)
        println("srFlag: " + srFlag)
        println("hasRun: " + hasRun)

        if (!hasRun){
            when{
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag ->{ multimodalOption() }
            }
        }

        if(hasRun){
            when{
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag ->{ startVoiceRecognition() }
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
                val speechStatus = textToSpeech!!.speak("Diga a linha que pretende para ver mais informações", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun multimodalOption() {
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
                        println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        println("Encerrou TTS")
                        if(activity != null && isAdded) {
                            startVoiceRecognition()
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak("Diga o nome da linha ou do botão em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition(){
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        if(isAdded && isVisible && getUserVisibleHint()) {
            runnable = Runnable {
                Speech.init(requireActivity())
                handler.sendEmptyMessage(0)
                hasInitSR = true
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "public transport speech recognition is now active")
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
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if (isAdded && isVisible && getUserVisibleHint()) {
                                            Speech.init(requireActivity())
                                            hasInitSR = true
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

}


/*
    fun ActionBar getActionBar(){
        return ((ActionBarActivity() activity?.getSupport
    }
*/
/*
@Override
public fun boolean onOptionsItemSelected() {
    /*
        ADD WHAT YOU WANT TO DO WHEN ARROW IS PRESSED
    */
    return super.onOptionsItemSelected(item);
}*/

//activity?.setActionBarTitle("TRANSPORTES PUBLICOS")

//(MainActivity getActivity()).setActionBarTitle("TRANSPORTES PUBLICOS")
//
// showBackButton()
//        // This callback will only be called when MyFragment is at least Started.
//        /*
//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
//            override fun handleOnBackPressed() {
//                val fragment: Fragment = TransportsFragment()
//                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//                fragmentManager.popBackStack()
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commit()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)*/
//
//        // The callback can be enabled or disabled here or in handleOnBackPressed()
//
//        /*
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        //enable menu
//        setHasOptionsMenu(true)
//
//        requireActivity()
//                .onBackPressedDispatcher
//                .addCallback(this) {
//                    println("ola")
//                }
//    }
// import kotlinx.android.synthetic.main.fragment_public_transports.*
//*/
// view?.findViewById<TextView>(R.id.public_transports_text)?.text = customText
//
// val photoView = view?.findViewById(R.id.photo_view) as PhotoView
//
//        //val photoView = photo_view as PhotoView
//        /*
//        button_view_timetable_1.setOnClickListener {
//
//            when(opSelected){
//                0 ->  { photoView.setImageResource(R.drawable.linha35_joaodedeus_crpg)
//                layoutInflater.inflate(R.layout.timetable_layout, null) }
//                1 ->  { photoView.setImageResource(R.drawable.linha35_joaodedeus_crpg)
//                        layoutInflater.inflate(R.layout.timetable_layout, null) }
//                2 ->  { photoView.setImageResource(R.drawable.linha45_joaodedeus_miramar)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//                3 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//                4 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//            }
//        }
//
//        button_view_timetable_2.setOnClickListener {
//            when(opSelected){
//                0 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//                1 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//                2 ->  { photoView.setImageResource(R.drawable.linha45_miramar_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//                3 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//                4 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
//                    layoutInflater.inflate(R.layout.timetable_layout, null) }
//            }
//        }
//        */
//
//        /*
//        when(pos){
//            1 -> {
//                text_to_from_1.text = "CRPG"
//                text_to_from_2.text = "S. João de Deus"
//                text_to_from_3.text = "S. João de Deus"
//                text_to_from_4.text = "CRPG"
//                opSelected = 1
//            }
//            2 -> {
//                textFromBusLines!!.text = "De Gaia para o CRPG"
//                opSelected = 2
//            }
//            3 -> {
//                textFromBusLines!!.text = "De Casa para o CRPG"
//                opSelected = 3
//            }
//        }
// val selItem: String = p0?.getItemAtPosition(p2).toString()*/