package com.github.vipulasri.timelineview.sample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/*
/**
 * Created by Vipul Asri on 05-12-2015.
 */
@Parcelize
class EventModel(
    var title: String,
    var info: String,
    var start_time: String,
    var end_time: String,
    var date: String
) : Parcelable*/

data class EventModel(
        val title: String,
        val info: String,
        val start_time: String,
        val end_time: String,
        val date: String
){
    override fun toString(): String {
        return "title: ${this.title}, info: ${this.info}, start_time: ${this.start_time}, " +
                "end_time: ${this.end_time}, date: ${this.date}"
    }
}
