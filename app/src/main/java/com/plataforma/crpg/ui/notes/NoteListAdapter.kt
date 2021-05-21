package com.plataforma.crpg.ui.notes

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.R
import com.plataforma.crpg.extentions.setVisible
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
        if (note.tipo == NoteType.TEXT) {
            holder.itemView.contentDescription = "Nota de texto nº ${position}: " +
                    "com o título ${note.titulo} e o conteúdo ${note.info}"
        }

        if (note.tipo == NoteType.VOICE) {
            holder.itemView.contentDescription = "Nota de voz nº ${position}: " +
                    "com o título ${note.titulo}, para ouvir pressione o botão de play"
        }

        var removedPosition : Int ? = null

        if (listData[position].tipo == NoteType.VOICE) {
            val uri: Uri = Uri.parse(listData[position].voiceNotePath)
            val player = SimpleExoPlayer.Builder(ctx).build()

            holder.itemView.note_item_player_view.player = player
            val mediaItem: MediaItem = MediaItem.fromUri(uri)
            player.setMediaItem(mediaItem)
        }


        holder.itemView.note_delete_icon.setOnClickListener{
            listData.removeAt(holder.adapterPosition)
            removedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onChange(listData)
        }

        holder.itemView.note_card_main.setOnClickListener {
            if (listData[position].tipo == NoteType.TEXT) {
                MaterialAlertDialogBuilder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                        .setTitle(listData[position].titulo)
                        .setMessage(listData[position].info)
                        .setNegativeButton("Fechar") { dialog, which -> }.show()
            }
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

}

class NoteViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.note_list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mContentView: TextView? = null
    private var mImageView: ImageView? = null
    private var mDeleteView: ImageView? = null
    private var mPlayerView: PlayerControlView? = null

    init {
        mTitleView = itemView.findViewById(R.id.item_title)
        mContentView = itemView.findViewById(R.id.item_description)
        mImageView = itemView.findViewById(R.id.note_image)
        mDeleteView = itemView.findViewById(R.id.note_delete_icon)
        mPlayerView = itemView.findViewById(R.id.note_item_player_view)
    }

    fun bind(note: Note) {
        mTitleView?.text = note.titulo
        mContentView?.text = note.info

        if (!note.imagePath.isBlank()) {
            mImageView?.setImageURI(note.imagePath.toUri())
        } else if (note.imagePath.isBlank() && note.tipo == NoteType.TEXT) {
            mImageView?.setImageResource(R.drawable.outline_create_black_24dp)
        }else if (note.imagePath.isBlank() && note.tipo == NoteType.VOICE) {
            mImageView?.setImageResource(R.drawable.outline_record_voice_over_black_24dp)
        }

        if(note.tipo == NoteType.TEXT){
            mContentView?.setVisible()
        }else if(note.tipo == NoteType.VOICE){
            mPlayerView?.setVisible()
        }

    }

}


//player.prepare()
//player.play()
//val uri: Uri = Uri.parse("android.resource://" + ctx.packageName.toString()
//        + "/raw/meditation_sound")

//val uri: Uri = Uri.parse("storage/emulated/0/VoiceNotes/1621590354570.wav")
//mImageView?.setImageURI(note.imagePath.toUri())
//val inputStream = getContentResolver().openInputStream(note.imagePath.toUri())
//AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
//println("Titulo: " + listData[position].titulo)
//println("Content: " + listData[position].info)
/*
fun deleteSelectedItem() {
    if(selectedList.isNotEmpty()){
        listData.removeAll{item -> item.selected == true}
    }
    notifyDataSetChanged()
}*/

//println("> List Data: $listData")
//println("> List Data: $position")
//println("> List Data: " + listData.size)
