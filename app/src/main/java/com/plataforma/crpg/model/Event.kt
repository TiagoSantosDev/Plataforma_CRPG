package com.plataforma.crpg.model

import com.google.gson.annotations.SerializedName

data class Event(
        var title: String,
        val info: String,
        var type: EventType,
        var start_time: String,
        val end_time: String,
        var date: String,
        val notes: String,
        var chosen_meal: String,
        var isLunch: Boolean,
        var meal_int: Int
){
    override fun toString(): String {
        return "title: ${this.title}, info: ${this.info}, type: ${this.type},  start_time: ${this.start_time}, " +
                "end_time: ${this.end_time}, date: ${this.date}"
    }
}

enum class EventType {
    @SerializedName("ACTIVITY")
    ACTIVITY,
    @SerializedName("MEAL")
    MEAL,
    @SerializedName("TRANSPORT")
    TRANSPORTS
}
