package com.plataforma.crpg.ui.notes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.windsekirun.naraeaudiorecorder.NaraeAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants
import com.github.windsekirun.naraeaudiorecorder.extensions.runOnUiThread
import com.github.windsekirun.naraeaudiorecorder.model.RecordMetadata
import com.github.windsekirun.naraeaudiorecorder.model.RecordState
import com.github.windsekirun.naraeaudiorecorder.source.DefaultAudioSource
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.textview.MaterialTextView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView.TAG
import com.plataforma.crpg.databinding.NewVoiceNoteFragmentBinding
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.new_voice_note_fragment.*
import kotlinx.android.synthetic.main.note_list_item.view.*
import java.io.File

class NewVoiceNoteFragment : Fragment(), AdapterView.OnItemSelectedListener {

    var myHandler: Handler? = null

    companion object {
        fun newInstance() = NewVoiceNoteFragment()
        const val PERMISSIONS_REQUEST_CODE = 1
    }

    //private var destFile: File? = null
    private lateinit var notesViewModel: NotesViewModel
    private var imageUri = ""
    private val audioRecorder = NaraeAudioRecorder()
    private var recordMetadata: RecordMetadata? = null
    private var destFile: File? = null
    private var isRecording: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = NewVoiceNoteFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    PERMISSIONS_REQUEST_CODE
            )
        }

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(
                        requireContext(),
                        LogConstants.PERMISSION_DENIED,
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "NOVA NOTA DE VOZ"

        notesViewModel = ViewModelProvider(activity as AppCompatActivity).get(NotesViewModel::class.java)
        val titleText = view.rootView?.findViewById<EditText>(R.id.voice_note_title_edit)?.text

        button_start_recording.setOnClickListener {

            val fileName = System.currentTimeMillis().toString()
            val extensions = ".wav"
            destFile = File(Environment.getExternalStorageDirectory(), "/VoiceNotes/$fileName$extensions")

            val recordConfig = AudioRecordConfig.defaultConfig()
            val audioSource = DefaultAudioSource(recordConfig)

            audioRecorder.create {
                this.destFile = this@NewVoiceNoteFragment.destFile
                this.recordConfig = recordConfig
                this.audioSource = audioSource
                this.timerCountCallback = { current, max -> timerChanged(current, max) }
                this.debugMode = true
            }

            //println("start recording check")
            audioRecorder.setOnRecordStateChangeListener { recordStateChanged(it) }
            audioRecorder.startRecording(requireContext())
            isRecording = true
            button_start_recording.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.md_blue_200))
        }

        button_stop_recording.setOnClickListener {
            if (isRecording) {
                audioRecorder.stopRecording()
                recordMetadata = audioRecorder.retrieveMetadata(destFile ?: File(""))
                println("Saved on ${destFile?.absolutePath}")

                button_start_recording.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.PrimaryGreen))
                button_stop_recording.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.md_blue_200))
                myHandler?.postDelayed(Runnable {
                    button_stop_recording.setBackgroundColor(resources.getColor(R.color.PrimaryGreen))
                }, 500)
            }
        }

        button_replay_recording.setOnClickListener{
            if (!destFile?.absolutePath.isNullOrBlank()) {
                val uri: Uri = Uri.parse(destFile?.absolutePath)
                val player = SimpleExoPlayer.Builder(requireContext()).build()

                val mediaItem: MediaItem = MediaItem.fromUri(uri)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
                button_replay_recording.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.md_blue_200))
                if(!player.isPlaying){
                    button_replay_recording.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.PrimaryGreen))
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
            if(titleText.toString().isNotBlank() && destFile?.absolutePath.toString().isNotBlank()) {
                view?.rootView?.findViewById<MaterialTextView>(R.id.aviso_titulo_voz_em_falta)?.visibility = GONE
                notesViewModel.newNote.tipo = NoteType.VOICE
                notesViewModel.newNote.titulo = voice_note_title_edit.text.toString()
                println("Absolute path voice note: " + destFile?.absolutePath.toString())
                notesViewModel.newNote.voiceNotePath = destFile?.absolutePath.toString()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

    private fun getVoiceItemCount(): Int{
        var voiceItemCount = 0

        for(item in notesViewModel.mNoteList){
            if (item.tipo == NoteType.VOICE)
                voiceItemCount++
        }

        return voiceItemCount
    }

    private fun timerChanged(currentTime: Long, maxTime: Long) {
        Log.d(TAG, "timerChanged: current: $currentTime max: $maxTime")
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Carregar imagem", true) -> button_new_voice_note_image.performClick()
            command.contains("Guardar nota", true) -> button_save_voice_note.performClick()
            command.contains("Adicionar título", true) -> voice_note_title_edit.requestFocus()
        }
    }

    private fun recordStateChanged(recordState: RecordState) {
        Log.d(TAG, "recordStateChanged: ${recordState.name}")

        runOnUiThread { println("RecordState: ${recordState.name}") }
    }

}


/*
//println(audioRecorder.toString())
//println("stop recording check ")
var contador = getVoiceItemCount()
println("Contador: $contador")
contador += 1
//val fileName = contador.toString().toLowerCase()
*/
/*
    val extensions = ".mp3"
    val audioRecorder = NaraeAudioRecorder()
    val destFile = File(Environment.getExternalStorageDirectory(), "/VoiceNotes/$fileName$extensions")
    audioRecorder.create {
        this.destFile = destFile
    }

    val recordConfig = AudioRecordConfig(MediaRecorder.AudioSource.MIC,
            AudioFormat.ENCODING_MP3,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioConstants.FREQUENCY_44100)
    val audioSource = NoiseAudioSource(recordConfig)

    audioRecorder.create {
        this.destFile = destFile
        this.recordConfig = recordConfig
        this.audioSource = audioSource
    }

    val recordAudioCode = 0

    requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), recordAudioCode)

    if (context?.let { ContextCompat.checkSelfPermission(it, arrayOf(Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())
            } != PackageManager.PERMISSION_GRANTED) {
*/
/*
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
            setDataSource(requireContext(), myUri)
            prepare()
            start()
        }
    }
}


        //val fileName = System.currentTimeMillis().asDateString()

*/

/*
//val recordConfig = AudioRecordConfig.defaultConfig()
//setDataSource(destFile.absolutePath)
//setDataSource(requireContext(), meditationUri)
//setDataSource(requireContext(), destFile.absolutePath.toUri())
val ffmpegAudioRecorder: FFmpegAudioRecorder = audioRecorder.getAudioRecorder() as? FFmpegAudioRecorder
        ?: return
context?.let { ffmpegAudioRecorder.setContext(it) }*/

//ffmpegAudioRecorder.setConvertConfig(FFmpegConvertConfig)

/*audioRecorder.create(FFmpegRecordFinder::class.java) {
    this.destFile = destFile
    this.recordConfig = recordConfig
    this.audioSource = audioSource
}*/
/*
//notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
    fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
*/

/*
            val uri = if (Build.VERSION.SDK_INT >= 24) {
                val authority = "$packageName.fileprovider"
                FileProvider.getUriForFile(requireContext(), authority, destFile ?: File(""))
            } else {
                Uri.fromFile(destFile)
            }

            val intent = Intent(Intent.ACTION_VIEW)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(destFile?.extension)
            intent.apply {
                setDataAndType(uri, mimeType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(intent, "Open with...")
            startActivity(chooser)
            */