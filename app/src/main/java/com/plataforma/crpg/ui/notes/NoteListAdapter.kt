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
import kotlinx.android.synthetic.main.note_list_item.view.*
import javax.security.auth.callback.Callback

class ListAdapter(private val list: List<Note>, private val onChange: (List<Note>) -> Unit) :
    RecyclerView.Adapter<NoteViewHolder>() {

    private var listData: MutableList<Note> = list.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(inflater, parent)
    }

    //substitui position por holder.adapterPosition
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = listData[holder.adapterPosition]

        holder.bind(note)
        println("> List Data: $listData")
        println("> List Data: $position")
        println("> List Data: " + listData.size)

        var removedPosition : Int ? = null

        holder.itemView.note_delete_icon.setOnClickListener{
            listData.removeAt(holder.adapterPosition)
            removedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onChange(listData)
        }
    }

    override fun getItemCount(): Int = listData.size
    /*
    fun deleteSelectedItem() {
        if(selectedList.isNotEmpty()){
            listData.removeAll{item -> item.selected == true}
        }
        notifyDataSetChanged()
    }*/
}

class NoteViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.note_list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mImageView: ImageView? = null
    private var mDeleteView: ImageView? = null

    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
        mImageView = itemView.findViewById(R.id.note_image)
        mDeleteView = itemView.findViewById(R.id.note_delete_icon)
    }

    fun bind(note: Note) {
        mTitleView?.text = note.titulo
        mYearView?.text = note.info
        mImageView?.setImageURI(note.imagePath.toUri())
        //mImageView?.setImageURI(note.imagePath.toUri())
        //val inputStream = getContentResolver().openInputStream(note.imagePath.toUri())
    }

}