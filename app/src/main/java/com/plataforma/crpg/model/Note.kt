package com.plataforma.crpg.model

data class Note(
        var tipo: NoteType,
        var data: String,
        var hora: String,
        var titulo: String,
        var info: String,
        var voiceNotePath: String,
        var imagePath: String
){
    override fun toString(): String {
        return "data: ${this.data},hora: ${this.hora}, titulo: ${this.titulo}, info: ${this.info}"
    }
}

enum class NoteType
{
    VOICE, TEXT
}