package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.NotesFragmentBinding
import com.plataforma.crpg.model.Note
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.notes_fragment.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class NotesFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = NotesFragment()
    }

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    private var textToSpeech: TextToSpeech? = null
    private lateinit var notesViewModel: NotesViewModel
    var voiceItemsCount = 0
    private val myLocale = Locale("pt_PT", "POR")
    private var hasInitSR = false


    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("notesHasRun", true).apply()
    }

    override fun onDestroy() {
        // Don't forget to shutdown!

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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = NotesFragmentBinding.inflate(layoutInflater)
        return binding.root
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "NOTAS"
        //notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("notesHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        notesViewModel = ViewModelProvider(activity as AppCompatActivity).get(NotesViewModel::class.java)
        notesViewModel.getNotesCollectionFromJSONWithoutPopulate()

        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapter(notesViewModel.mNoteList, context
            ) { newList -> notesViewModel.mNoteList = newList as ArrayList<Note> }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_new_voice_note.setOnClickListener {
            val fragment: Fragment = NewVoiceNoteFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        button_new_text_note.setOnClickListener {
            val fragment: Fragment = NewTextNoteFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    fun getVoiceItemCount(): Int{
        var voiceItemCount = 0

        for(item in notesViewModel.mNoteList){
            if (item.tipo == NoteType.VOICE)
                voiceItemCount++
        }

        return voiceItemCount
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
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
                val speechStatus = textToSpeech!!.speak("Diga Nota Voz ou Nota Texto em voz alta para criar uma nova nota", TextToSpeech.QUEUE_FLUSH, null, "ID")
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

                    val speechStatus = textToSpeech!!.speak("Diga Nota Voz ou Nota Texto em voz alta para criar uma nova nota", TextToSpeech.QUEUE_FLUSH, null, "ID")

                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun startVoiceRecognition(){
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
                        if(activity != null && isAdded) {
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

    fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Nota Texto", true) -> button_new_text_note.performClick()
            command.contains("Nota Voz", true) -> button_new_voice_note.performClick()
        }
    }

}

//if (this::runnable.isInitialized)
/*
                        performActionWithVoiceCommand(result)
                        Log.i("speech", "result: $result")
                        println("on Speech Result")
                        hasInitSR = false
                        */
/*
if (hasInitSR) {
    if (Speech.getInstance() != null) {
        Speech.getInstance().stopListening()
        Speech.getInstance().shutdown()
        println("shutdown Speech")
    }
}*/

/*
                    var handler = Handler(Looper.getMainLooper())
                    //var runable = Runnable {
<Button
android:id="@+id/button_consult_transport"
android:layout_width="331dp"
android:layout_height="wrap_content"
android:layout_gravity="center|bottom"
android:layout_marginBottom="28dp"
android:text="@string/ver_transportes_publicos"
android:textSize="20sp"
app:backgroundTint="@color/md_blue_100"
app:cornerRadius="8dp"
app:layout_constraintBottom_toBottomOf="parent"
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintStart_toStartOf="parent" />


private val noteList = listOf(
            Note(NoteType.TEXT, "16042021","1200", "Nota 1",
                    "a","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 2",
                    "b","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 3",
                    "c","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg")
    ) */

//adapter = ListAdapter(notesViewModel.noteList
//adapter = ListAdapter(notesViewModel.noteList
//) { newList -> notesViewModel.mNoteList = newList as ArrayList<Note> }