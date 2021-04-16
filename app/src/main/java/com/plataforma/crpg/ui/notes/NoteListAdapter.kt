package com.plataforma.crpg.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.R
import com.plataforma.crpg.model.Note

class ListAdapter(private val list: List<Note>) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val note: Note = list[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = list.size
}

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.note_list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null

    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
    }

    fun bind(note: Note) {
        mTitleView?.text = note.titulo
        mYearView?.text = note.info
    }
}
