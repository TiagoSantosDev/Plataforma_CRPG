package com.plataforma.crpg.ui.notes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.windsekirun.naraeaudiorecorder.NaraeAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.constants.AudioConstants
//import com.github.windsekirun.naraeaudiorecorder.ffmpeg.FFmpegAudioRecorder
//import com.github.windsekirun.naraeaudiorecorder.ffmpeg.FFmpegRecordFinder
//import com.github.windsekirun.naraeaudiorecorder.ffmpeg.config.FFmpegConvertConfig
import com.github.windsekirun.naraeaudiorecorder.source.NoiseAudioSource
import com.google.android.material.textview.MaterialTextView
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NewVoiceNoteFragmentBinding
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.new_voice_note_fragment.*
import java.io.File
import kotlin.math.absoluteValue


class NewVoiceNoteFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = NewVoiceNoteFragment()
    }

    private lateinit var notesViewModel: NotesViewModel
    private var imageUri = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = NewVoiceNoteFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        //showBackButton()

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "NOVA NOTA DE VOZ"
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notesViewModel = ViewModelProvider(activity as AppCompatActivity).get(NotesViewModel::class.java)

        val titleText = view?.rootView?.findViewById<EditText>(R.id.voice_note_title_edit)?.text
        var contador = getVoiceItemCount()
        println("Contador: $contador")

        contador += 1
        val fileName = contador.toString().toLowerCase()
        println(fileName)
        val extensions = ".mp3"
        val audioRecorder = NaraeAudioRecorder()
        val destFile = File(Environment.getExternalStorageDirectory(), "/VoiceNotes/$fileName$extensions")
        audioRecorder.create {
            this.destFile = destFile
        }

        //val recordConfig = AudioRecordConfig.defaultConfig()

        val recordConfig = AudioRecordConfig(MediaRecorder.AudioSource.MIC,
                AudioFormat.ENCODING_MP3,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioConstants.FREQUENCY_44100)
        val audioSource = NoiseAudioSource(recordConfig)
        /*audioRecorder.create(FFmpegRecordFinder::class.java) {
            this.destFile = destFile
            this.recordConfig = recordConfig
            this.audioSource = audioSource
        }*/

        audioRecorder.create {
            this.destFile = destFile
            this.recordConfig = recordConfig
            this.audioSource = audioSource
        }

        /*
        val ffmpegAudioRecorder: FFmpegAudioRecorder = audioRecorder.getAudioRecorder() as? FFmpegAudioRecorder
                ?: return
        context?.let { ffmpegAudioRecorder.setContext(it) }*/

        //ffmpegAudioRecorder.setConvertConfig(FFmpegConvertConfig)

        val recordAudioCode = 0

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),recordAudioCode)

        if (context?.let {
                    ContextCompat.checkSelfPermission(it,
                            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())
                } != PackageManager.PERMISSION_GRANTED) {

            button_start_recording.setOnClickListener {
                audioRecorder.startRecording(requireActivity())
            }

            button_stop_recording.setOnClickListener {
                audioRecorder.stopRecording()
                println("Dest File exists: " + destFile.exists())
            }

            button_replay_recording.setOnClickListener {
                val myUri = Uri.parse(destFile.absolutePath)
                val meditationUri = Uri.parse("android.resource://" + context?.packageName.toString()
                    + "/raw/meditation_sound")
                println("Dest File exists: " + myUri.path)
                println("Dest file path: " + destFile.absolutePath)
                //val myUri: Uri = Environment.getExternalStorageDirectory().absolutePath // initialize Uri here
                val mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                            AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    )
                    //setDataSource(destFile.absolutePath)
                    //setDataSource(requireContext(), meditationUri)
                    //setDataSource(requireContext(), destFile.absolutePath.toUri())
                    setDataSource(requireContext(), myUri)
                    prepare()
                    start()
                }

            }

        }

        button_new_voice_note_image.setOnClickListener{
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
        }

        button_save_voice_note.setOnClickListener{

            if(titleText.toString().isNotBlank() && destFile.exists()) {
                view?.rootView?.findViewById<MaterialTextView>(R.id.aviso_titulo_voz_em_falta)?.visibility = GONE
                notesViewModel.newNote.tipo = NoteType.VOICE
                notesViewModel.newNote.titulo = voice_note_title_edit.text.toString()
                notesViewModel.newNote.voiceNotePath = destFile.absolutePath
                notesViewModel.newNote.imagePath = imageUri
                notesViewModel.addNewVoiceNote()

                val fragment: Fragment = NotesFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }else{
                view?.rootView?.findViewById<MaterialTextView>(R.id.aviso_titulo_voz_em_falta)?.visibility = VISIBLE
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            voice_note_image.setImageURI(fileUri)
            imageUri = fileUri.toString()
            println("Uri: $imageUri")

            val file: File = ImagePicker.getFile(data)!!
            val filePath:String = ImagePicker.getFilePath(data)!!

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            println(">Erro a apresentar a foto")
        } else {
            println(">Escolha de foto cancelada")
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

}

/*
//notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
    fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
*/