package com.plataforma.crpg.ui.notes

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NewTextNoteFragmentBinding
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.new_text_note_fragment.*


class NewTextNoteFragment : Fragment() {

    companion object {
        fun newInstance() = NewTextNoteFragment()
    }

    val RESULT_GALLERY = 0
    var imageUri = ""
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

        button_get_image_from_gallery.setOnClickListener {

            val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, RESULT_GALLERY)

        }

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
                println("Image Uri: " + imageUri)
                //Do whatever that you desire here. or leave this blank
            }
            else -> {
            }
        }
    }

}