package com.plataforma.crpg.model

data class Event(
        var title: String,
        val info: String,
        var type: EventType,
        var start_time: String,
        val end_time: String,
        var date: String,
        val notes: String,
){
    override fun toString(): String {
        return "title: ${this.title}, info: ${this.info}, start_time: ${this.start_time}, " +
                "end_time: ${this.end_time}, date: ${this.date}"
    }
}

enum class EventType {
    ACTIVITY, MEAL, TRANSPORTS
}
