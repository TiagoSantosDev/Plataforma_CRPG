package com.github.vipulasri.timelineview.sample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.vipulasri.timelineview.sample.model.EventModel
import java.util.ArrayList

class EventDataViewModel(application: Application) : AndroidViewModel(application) {

    val mDataList = ArrayList<EventModel>()

    private fun getDataFromJson() {
    }

    fun setDataListItems(): ArrayList<EventModel> {

        mDataList.add(
            EventModel(
                "evento1", "a", "11:00", "12:00",
                "2017-02-12"
            )
        )
        mDataList.add(
            EventModel(
                "evento1", "a", "11:00", "12:00",
                "2017-02-12"
            )
        )
        mDataList.add(
            EventModel(
                "evento2", "a", "11:00", "12:00",
                "2017-02-11"
            )
        )
        mDataList.add(
            EventModel(
                "evento3", "a", "11:00", "12:00",
                "2017-02-11"
            )
        )
        mDataList.add(
            EventModel(
                "evento4", "a", "11:00", "12:00",
                "2017-02-11"
            )
        )
        mDataList.add(
            EventModel(
                "evento5", "a", "11:00", "12:00",
                "2017-02-11"
            )
        )
        mDataList.add(
            EventModel(
                "evento6", "a", "11:00", "12:00",
                "2017-02-10"
            )
        )

        return mDataList
    }
}
