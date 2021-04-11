package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.windsekirun.naraeaudiorecorder.NaraeAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.source.NoiseAudioSource
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NotesFragmentBinding
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.fragment_date_picker.*
import kotlinx.android.synthetic.main.new_voice_note_fragment.*
import kotlinx.android.synthetic.main.transports_fragment.*
import java.io.File


class NewVoiceNoteFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = NewVoiceNoteFragment()
    }

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = NotesFragmentBinding.inflate(layoutInflater)

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        val fileName = "one"
        val extensions = ".pcm"
        val audioRecorder = NaraeAudioRecorder()
        val destFile = File(Environment.getExternalStorageDirectory(), "/VoiceNotes/$fileName$extensions")
        audioRecorder.create() {
            this.destFile = destFile
        }

        val recordConfig = AudioRecordConfig.defaultConfig()
        val audioSource = NoiseAudioSource(recordConfig)
        audioRecorder.create() {
            this.destFile = this.destFile
            this.recordConfig = recordConfig
            this.audioSource = audioSource
        }

        button_start_recording.setOnClickListener {
            audioRecorder.startRecording(requireActivity())
        }

        button_stop_recording.setOnClickListener {
            audioRecorder.stopRecording()
        }


        button_replay_recording.setOnClickListener {

            val myUri = Uri.parse(destFile.absolutePath)
            //val myUri: Uri = Environment.getExternalStorageDirectory().absolutePath // initialize Uri here
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                )
                setDataSource(activity, myUri)
                prepare()
                start()
            }

        }

        button_save_voice_note.setOnClickListener{
            notesViewModel.newNote.tipo = NoteType.VOICE
            notesViewModel.newNote.titulo = ""
            notesViewModel.newNote.voiceNotePath = destFile.absolutePath
            notesViewModel.newNote.tipo =
            notesViewModel.newNote.tipo
        }




    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}