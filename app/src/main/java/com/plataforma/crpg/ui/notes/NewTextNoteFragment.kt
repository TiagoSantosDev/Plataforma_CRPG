package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NewTextNoteFragmentBinding
import com.plataforma.crpg.databinding.NotesFragmentBinding
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.fragment_date_picker.*
import kotlinx.android.synthetic.main.new_text_note_fragment.*
import kotlinx.android.synthetic.main.transports_fragment.*


class NewTextNoteFragment : Fragment() {

    companion object {
        fun newInstance() = NewTextNoteFragment()
    }

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = NewTextNoteFragmentBinding.inflate(layoutInflater)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        val titleText = view?.rootView?.findViewById<EditText>(R.id.title_nova_nota_texto)?.text
        val contentText = view?.rootView?.findViewById<EditText>(R.id.conteudo_nota)?.text
        val imagePath : String

        button_save_text_note.setOnClickListener {
            notesViewModel.newNote.tipo = NoteType.TEXT
            notesViewModel.newNote.titulo = titleText.toString()
            notesViewModel.newNote.info = contentText.toString()
            //notesViewModel.newNote.imagePath = imagePath


        }

    }


}