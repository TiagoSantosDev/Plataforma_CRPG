package com.plataforma.crpg.ui.notes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textview.MaterialTextView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.NewTextNoteFragmentBinding
import com.plataforma.crpg.model.NoteType
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.new_text_note_fragment.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.io.File
import java.lang.Boolean.FALSE
import java.util.*
import kotlin.properties.Delegates

class NewTextNoteFragment : Fragment() {

    companion object {
        fun newInstance() = NewTextNoteFragment()
        //image pick code
        const val IMAGE_PICK_CODE = 1000

        //Permission code
        const val PERMISSION_CODE = 1001
    }

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    private var textToSpeech: TextToSpeech? = null

    val RESULT_GALLERY = 0
    private var imageUri = ""
    val listen : MutableLiveData<Boolean> =  MutableLiveData<Boolean>()
    private lateinit var notesViewModel: NotesViewModel
    private val myLocale = Locale("pt_PT", "POR")

    override fun onDestroy() {
        // Don't forget to shutdown!

        handler.removeCallbacksAndMessages(null)

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("removed callbacks")
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
        val binding = NewTextNoteFragmentBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "NOVA NOTA DE TEXTO"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel = ViewModelProvider(activity as AppCompatActivity).get(NotesViewModel::class.java)

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("notesTextHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        val titleText = view?.rootView?.findViewById<EditText>(R.id.titulo_edit_text)?.text
        val contentText = view?.rootView?.findViewById<EditText>(R.id.conteudo_nota)?.text
        val imagePath : String

        button_get_image_from_gallery.setOnClickListener{
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
        }


        button_save_text_note.setOnClickListener {

            if(titleText.toString().isNotBlank() && contentText.toString().isNotBlank()) {
                view?.rootView?.findViewById<MaterialTextView>(R.id.aviso_dados_falta_texto)?.visibility = View.GONE
                println("> Passou a validacao")
                println("Title text: " + titleText.toString())
                println("Title text: " + contentText.toString())
                notesViewModel.newNote.tipo = NoteType.TEXT
                notesViewModel.newNote.titulo = titleText.toString()
                notesViewModel.newNote.info = contentText.toString()
                notesViewModel.newNote.imagePath = imageUri
                notesViewModel.addNewTextNote()

                val fragment: Fragment = NotesFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }else{
                view?.rootView?.findViewById<MaterialTextView>(R.id.aviso_dados_falta_texto)?.visibility = View.VISIBLE
            }

        }

    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {

        println("ttsFlag:  " + ttsFlag)
        println("srFlag: " + srFlag)
        println("hasRun: " + hasRun)

        //conteudo_nota.requestFocus()

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
                    Log.e("TTS", "Linguagem n??o suportada!")
                }
                val speechStatus = textToSpeech!!.speak("Diga T??tulo ou Conteudo em voz alta para iniciar escrita", TextToSpeech.QUEUE_FLUSH, null, "ID")
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

                val speechStatus = textToSpeech!!.speak("Diga T??tulo ou Conteudo em voz alta para iniciar escrita", TextToSpeech.QUEUE_FLUSH, null, "ID")

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
                handler.sendEmptyMessage(0);
                Speech.init(requireActivity())
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "new text note speech recognition is now active")
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
                                        if(isAdded && isVisible && getUserVisibleHint()) {
                                            Speech.init(requireActivity())
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

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            note_image.setImageURI(fileUri)
            imageUri = fileUri.toString()
            println("Uri: $imageUri")

            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            //Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            println(">Erro a apresentar a foto")
        } else {
            //Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            println(">Escolha de foto cancelada")
        }


    }

    private var IMAGE_PICKED: Boolean by Delegates.observable(FALSE) { property, oldValue, newValue ->
        println("New Value $newValue")
        println("Old Value $oldValue")
    }

    fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Carregar imagem", true) -> button_get_image_from_gallery.performClick()
            command.contains("Guardar", true) -> button_save_text_note.performClick()
            command.contains("T??tulo", true) -> titulo_edit_text.requestFocus()
            command.contains("Conte??do", true) -> conteudo_nota.requestFocus()
        }
    }

}



/*
        //println("Notes view model value : " + notesViewModel.mNoteList)

        when (requestCode) {
            RESULT_GALLERY -> if (null != data) {
                imageUri = data.data.toString()
                IMAGE_PICKED = TRUE
                println("Image Uri: " + imageUri)
                //Do whatever that you desire here. or leave this blank
            }
            else -> {
            }
        }
        var imageNewPath = "/storage/emulated/0/Pictures/IMG_20210411_215347.jpg"
        val imgFile = File(imageNewPath)
        //val imgFileToPatch = imgFile.toPath()

        println("Exists: " + imgFile.exists())
        println("Can read: " + imgFile.canRead())
        println("Absolute Path: " + imgFile.absolutePath)


        if (imgFile.exists()) {
            println("Imagem existe!")
            val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val myImage: ImageView = view?.findViewById(R.id.note_image) as ImageView
            myImage.setImageBitmap(myBitmap)
        }
    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                pickImageFromGallery()
            }
        }
    }



when (requestCode) {
    val targetUri: Uri = data!!.data
    textTargetUri.setText(targetUri.toString())
    val bitmap: Bitmap
    try {
        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri))
        targetImage.setImageBitmap(bitmap)
    } catch (e: FileNotFoundException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
}
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((context as BaseActivity?)!!, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK)
        } else {


        }

        //var file = File(imageNewPath)
        ///storage/emulated/0/Pictures/IMG_20210411_215347.jpg
        //var getExtDir = requireActivity().getExternalFilesDir(null)
        //println(getExtDir)
        //val imgFile = File(imageUri)


        val imageUri: Uri = data.getData()
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)

button_get_image_from_gallery.setOnClickListener {

    val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(galleryIntent, RESULT_GALLERY)

}


    listen.value = IMAGE_PICKED
    listen.observe(requireActivity(), Observer {

        println("IMAGE_PICKED FOI SET A TRUE")

        val imgFile = File(imageUri)
        //val imgFileToPatch = imgFile.toPath()

        println("Can read:" + imgFile.canRead())
        println("Path " + imgFile.absolutePath)
        //println("Can " + imgFile.toPath())

        if (imgFile.exists()) {
            val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val myImage: ImageView = view?.findViewById(R.id.note_image) as ImageView
            myImage.setImageBitmap(myBitmap)
        }

    })*/

/*
    private val noteList = listOf(
            Note(NoteType.TEXT, "16042021","1200", "Nota 1",
                    "a","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_184527541.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 2",
                    "b","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_184527541.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 3",
                    "c","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_184527541.jpg"),
    ) list_recycler_view.apply {
    val layoutManager = LinearLayoutManager(activity)
    val adapter = ListAdapter(noteList)
}*/