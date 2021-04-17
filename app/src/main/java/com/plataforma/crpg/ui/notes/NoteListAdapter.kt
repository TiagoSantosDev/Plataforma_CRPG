package com.plataforma.crpg.ui.notes

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.R
import com.plataforma.crpg.model.Note
import java.io.InputStream

class ListAdapter(private val list: List<Note>) :
    RecyclerView.Adapter<NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = list[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = list.size
}

class NoteViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.note_list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mImageView: ImageView? = null

    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
        mImageView = itemView.findViewById(R.id.note_image)
    }

    fun bind(note: Note) {
        mTitleView?.text = note.titulo
        mYearView?.text = note.info
        mImageView?.setImageURI(note.imagePath.toUri())
        //mImageView?.setImageURI(note.imagePath.toUri())
        //val inputStream = getContentResolver().openInputStream(note.imagePath.toUri())
    }
}
//mImageView.setIma