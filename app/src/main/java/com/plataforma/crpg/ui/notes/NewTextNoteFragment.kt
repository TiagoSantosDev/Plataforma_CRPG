package com.plataforma.crpg.ui.notes

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NewTextNoteFragmentBinding
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.new_text_note_fragment.*
import java.io.File
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.jar.Manifest
import kotlin.properties.Delegates


class NewTextNoteFragment : Fragment() {

    companion object {
        fun newInstance() = NewTextNoteFragment()
    }

    val RESULT_GALLERY = 0
    //var IMAGE_PICKED = FALSE
    var imageUri = ""
    val listen : MutableLiveData<Boolean> =  MutableLiveData<Boolean>()
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = NewTextNoteFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        val titleText = view?.rootView?.findViewById<EditText>(R.id.titulo_edit_text)?.text
        val contentText = view?.rootView?.findViewById<EditText>(R.id.conteudo_nota)?.text
        val imagePath : String

        /*
        val imageUri: Uri = data.getData()
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)
        */
        button_get_image_from_gallery.setOnClickListener {

            val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, RESULT_GALLERY)

        }

    /*
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

        })
    */


        button_save_text_note.setOnClickListener {
            notesViewModel.newNote.tipo = NoteType.TEXT
            notesViewModel.newNote.titulo = titleText.toString()
            notesViewModel.newNote.info = contentText.toString()
            notesViewModel.newNote.imagePath = imageUri


            val fragment: Fragment = NotesFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


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


    }


    private var IMAGE_PICKED: Boolean by Delegates.observable(FALSE) { property, oldValue, newValue ->
        println("New Value $newValue")
        println("Old Value $oldValue")


        //Path da imagem: /data/media/0/Pictures/IMG_20210411_215349.jpg
        //var imageNewPath = "/data/media/0/Pictures/IMG_20210411_215349.jpg"



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
    }
/*
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

*/

}
/*
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

*/