package com.github.vipulasri.timelineview.sample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
) : Parcelable
