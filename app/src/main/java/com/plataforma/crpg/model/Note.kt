package com.plataforma.crpg.model

import android.graphics.Bitmap

data class Note(
    val data: String,
    val hora: String,
    val tipo: NoteType,
    val titulo: String,
    val info: String,
    val imagem: Bitmap
){
    override fun toString(): String {
        return "data: ${this.data},hora: ${this.hora}, titulo: ${this.titulo}, info: ${this.info}"
    }
}

enum class NoteType
{
    VOICE, TEXT
}