package com.plataforma.crpg.model


data class EventModel(
        var title: String,
        val info: String,
        var start_time: String,
        val end_time: String,
        var date: String,
        val notas: String,
        var alarm_type: AlarmType,
        var alarm_freq: AlarmFrequency
){
    override fun toString(): String {
        return "title: ${this.title}, info: ${this.info}, start_time: ${this.start_time}, " +
                "end_time: ${this.end_time}, date: ${this.date}"
    }
}

enum class AlarmType {
    SOM, VIBRAR, AMBOS
}

enum class AlarmFrequency {
    HOJE, AMANHA, TODOS_OS_DIAS, PERSONALIZADO
}
