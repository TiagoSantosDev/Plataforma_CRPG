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
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import com.github.chrisbanes.photoview.PhotoView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.FragmentPublicTransportsTimetablesBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.fragment_custom_transport.button_return_transports
import kotlinx.android.synthetic.main.fragment_public_transport.*
import kotlinx.android.synthetic.main.fragment_public_transports_timetables.*
import kotlinx.android.synthetic.main.timetable_layout.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class PublicTransportsTimetableFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var opSelected = 0
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var transportsViewModel: TransportsViewModel

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()
    private var textToSpeech: TextToSpeech? = null
    private val myLocale = Locale("pt_PT", "POR")
    private var hasInitSR = false

    companion object {
        private const val trindadeString = "Trindade"
        private const val valadaresString = "Valadares"
        private const val francelosString = "Francelos"
        private const val crpgString = "CRPG"
        private const val miramarString = "Miramar"
        private const val joaoDeDeusString = "João de Deus"
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("publicTransportTimetableHasRun", true).apply()
    }

    override fun onDestroy() {
        // Don't forget to shutdown!
        handler.removeCallbacks(runnable);

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

        super.onDestroy ();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view?.findViewById<View>(R.id.photo_view)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.photo_view_hint)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.frame_layout_timetables)?.visibility = VISIBLE

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
        val binding = FragmentPublicTransportsTimetablesBinding.inflate(layoutInflater)
        val view = binding.root

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("publicTransportTimetableHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        val linesSpinner: Spinner? = view.findViewById(R.id.bus_lines_spinner_2)
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
        (activity as AppCompatActivity).supportActionBar?.title = "VER HORÁRIO"

        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        val selectedDate = sharedViewModel.selectedDate
        val publicText = transportsViewModel.getPublicTransportText(selectedDate)
        println("Public custom text: $publicText")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //val binding = FragmentPublicTransportsTimetablesBinding.inflate(layoutInflater)
        val photoView = photo_view as PhotoView

        fun imageViewerController() {
            view?.findViewById<View>(R.id.photo_view_hint)?.visibility = VISIBLE
            view?.findViewById<View>(R.id.photo_view)?.visibility = VISIBLE
            view?.findViewById<View>(R.id.frame_layout_timetables)?.visibility = INVISIBLE
            view?.findViewById<View>(R.id.photo_view)?.setOnClickListener {
                view?.findViewById<View>(R.id.photo_view)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.photo_view_hint)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.frame_layout_timetables)?.visibility = VISIBLE
            }
        }

        button_view_timetable_1.setOnClickListener {

            when(opSelected){
                1 ->  { photoView.setImageResource(R.drawable.stcp_901_trindade_valadares)
                    imageViewerController()}
                2 ->  { photoView.setImageResource(R.drawable.zf_valadares_francelos)
                    imageViewerController() }
                3 ->  { photoView.setImageResource(R.drawable.linha35_joaodedeus_crpg)
                    imageViewerController() }
                4 ->  { photoView.setImageResource(R.drawable.linha45_joaodedeus_miramar)
                    imageViewerController() }
            }
        }

        button_view_timetable_2.setOnClickListener {
            when(opSelected){
                1 ->  { photoView.setImageResource(R.drawable.stcp_901_valadares_trindade)
                    imageViewerController() }
                2 ->  { photoView.setImageResource(R.drawable.zf_francelos_valadares)
                    imageViewerController() }
                3 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    imageViewerController() }
                4 ->  { photoView.setImageResource(R.drawable.linha45_miramar_joaodedeus)
                    imageViewerController() }
            }
        }

        button_return_transports.setOnClickListener {
            val fragment: Fragment = PublicTransportsFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        fun performActionWithVoiceCommand(command: String){
            when {
                command.contains("Linha 901", true) -> bus_lines_spinner_2.setSelection(0)
                command.contains("Linha ZF", true) -> bus_lines_spinner_2.setSelection(1)
                command.contains("Linha 35", true) -> bus_lines_spinner_2.setSelection(2)
                command.contains("Linha 45", true) -> bus_lines_spinner_2.setSelection(3)
                command.contains("Ver horário de ida", true) -> button_view_timetable_1.performClick()
                command.contains("Ver horário de chegada", true) -> button_view_timetable_2.performClick()
                command.contains("Regressar", true) -> button_return_transports.performClick()
            }
        }

        Speech.init(context)

        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
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
                    performActionWithVoiceCommand(result)
                    Log.i("speech", "result: $result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")

        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        //println("Item clicked: $p2")
        when(p2){
            0 -> {
                text_to_from_1.text = trindadeString
                text_to_from_2.text = valadaresString
                text_to_from_3.text = valadaresString
                text_to_from_4.text = trindadeString
                opSelected = 1
            }
            1 -> {
                text_to_from_1.text = valadaresString
                text_to_from_2.text = francelosString
                text_to_from_3.text = francelosString
                text_to_from_4.text = valadaresString
                opSelected = 2
            }
            2 -> {
                text_to_from_1.text = joaoDeDeusString
                text_to_from_2.text = crpgString
                text_to_from_3.text = crpgString
                text_to_from_4.text = joaoDeDeusString
                opSelected = 3
            }
            3 -> {
                text_to_from_1.text = joaoDeDeusString
                text_to_from_2.text = miramarString
                text_to_from_3.text = miramarString
                text_to_from_4.text = joaoDeDeusString
                opSelected = 4
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
            //frame_de_para_1.visibility = GONE
            //frame_de_para_2.visibility = GONE
    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun performActionWithVoiceCommand(command: String) {
        when {
            command.contains("901", true) || command.contains("novecentos e um", true) -> bus_lines_spinner.setSelection(0)
            command.contains("ZF", true) -> bus_lines_spinner.setSelection(1)
            command.contains("35", true) || command.contains("trinta e cinco", true) -> bus_lines_spinner.setSelection(2)
            command.contains("45", true) || command.contains("quarenta e cinco", true)-> bus_lines_spinner.setSelection(3)
            command.contains("ir", true) -> button_view_timetable_1.performClick()
            command.contains("voltar", true) -> button_view_timetable_2.performClick()
        }
    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {

        println("ttsFlag:  " + ttsFlag)
        println("srFlag: " + srFlag)
        println("hasRun: " + hasRun)

        if (!hasRun) {
            when {
                ttsFlag && !srFlag -> {
                    startTTS()
                }
                !ttsFlag && srFlag -> {
                    startVoiceRecognition()
                }
                ttsFlag && srFlag -> {
                    multimodalOption()
                }
            }
        }

        if (hasRun) {
            when {
                !ttsFlag && srFlag -> {
                    startVoiceRecognition()
                }
                ttsFlag && srFlag -> {
                    startVoiceRecognition()
                }
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
                val speechStatus = textToSpeech!!.speak("Diga o nome da linha e depois diga ida ou volta para ver o horário correspondente", TextToSpeech.QUEUE_FLUSH, null, "ID")
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
                        if (activity != null && isAdded) {
                            startVoiceRecognition()
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak("Diga o nome da linha e depois diga ida ou volta para ver o horário correspondente", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition() {
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            handler.sendEmptyMessage(0);
            Speech.init(requireActivity())
            hasInitSR = true
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
                        if (activity != null && isAdded) {
                            handler.postDelayed({
                                try {
                                    Speech.init(requireActivity())
                                    hasInitSR = true
                                    Speech.getInstance().startListening(this)
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



/*
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PublicTransportsTimetableFragment().apply {
                }
    }
*/
//val photoView = view?.findViewById(R.id.photo_view) as PhotoView
//textFromBusLines!!.text = "De Casa para o CRPG"
//val selItem: String = p0?.getItemAtPosition(p2).toString()
//var textFromBusLines = view?.rootView?.findViewById<TextView>(R.id.)
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
// public_transports_text.text= publicText
//
// layoutInflater.inflate(R.layout.timetable_layout, null).bringToFront()
//                    //view?.findViewById<View>(R.id.timetable
//
// layoutInflater.inflate(R.layout.timetable_layout, null)