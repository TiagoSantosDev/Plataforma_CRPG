package com.plataforma.crpg.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.R
import com.plataforma.crpg.model.Note
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.note_list_item.view.*

class ListAdapter(private val list: List<Note>, private val ctx: Context, private val onChange: (List<Note>) -> Unit) :
    RecyclerView.Adapter<NoteViewHolder>() {

    private var listData: MutableList<Note> = list.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(inflater, parent)
    }

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

        holder.itemView.note_card_main.setOnClickListener{
            //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
            println("Titulo: " + listData[position].titulo)
            println("Content: " + listData[position].info)

            MaterialAlertDialogBuilder(ctx,android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle(listData[position].titulo)
                    .setMessage(listData[position].info)
                    .setNegativeButton("Fechar"){ dialog, which -> }.show()
        }

    }

    override fun getItemCount(): Int = listData.size


    fun getVoiceItemCount(): Int{
        var voiceItemCount = 0

        for(item in listData){
            if (item.tipo == NoteType.VOICE)
            voiceItemCount++
        }

        return voiceItemCount
    }
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
        if (!note.imagePath.isBlank()){
            mImageView?.setImageURI(note.imagePath.toUri())
        }else if(note.imagePath.isBlank() && note.tipo == NoteType.TEXT){
            mImageView?.setImageResource(R.drawable.outline_create_black_24dp)
        }else{
            mImageView?.setImageResource(R.drawable.outline_record_voice_over_black_24dp)
        }

        //mImageView?.setImageURI(note.imagePath.toUri())
        //val inputStream = getContentResolver().openInputStream(note.imagePath.toUri())
    }

}