package com.github.vipulasri.timelineview.sample


import androidx.lifecycle.ViewModel
import com.github.vipulasri.timelineview.sample.model.EventModel
import java.util.ArrayList

class JSONEventParser : ViewModel(){

    private val mDataList = ArrayList<EventModel>()

    private fun getDataFromJson(){


    }

    private fun setDataListItems() {

        mDataList.add(EventModel("Item successfully delivered", "a", "11:00", "12:00", "2017-02-12 08:00"))
        mDataList.add(EventModel("Courier is out to delivery your order", "a", "11:00", "12:00", "2017-02-12 08:00"))
        mDataList.add(EventModel("Item has reached courier facility at New Delhi", "a", "11:00", "12:00", "2017-02-11 21:00"))
        mDataList.add(EventModel("Item has been given to the courier", "a", "11:00", "12:00", "2017-02-11 18:00"))
        mDataList.add(EventModel("Item is packed and will dispatch soon", "a", "11:00", "12:00", "2017-02-11 09:30"))
        mDataList.add(EventModel("Order is being readied for dispatch", "a", "11:00", "12:00", "2017-02-11 08:00"))
        mDataList.add(EventModel("Order processing initiated", "a", "11:00", "12:00", "2017-02-10 15:00"))
        mDataList.add(EventModel("Order confirmed by seller", "a", "11:00", "12:00", "2017-02-10 14:30"))
        mDataList.add(EventModel("Order placed successfully", "a", "11:00", "12:00", "2017-02-10 14:00"))
    }

}
