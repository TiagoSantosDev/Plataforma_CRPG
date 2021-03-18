package com.plataforma.crpg.example

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.BaseActivity
import com.plataforma.crpg.R
import com.plataforma.crpg.model.EventModel
import kotlinx.android.synthetic.main.activity_example.*
import java.util.ArrayList

/**
 * Created by Vipul Asri on 13-12-2018.
 */
class ExampleActivity : BaseActivity() {

    private lateinit var mAdapter: ExampleTimeLineAdapter
    private val mDataList = ArrayList<EventModel>()
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        setActivityTitle(getString(R.string.activity_example_label))
        isDisplayHomeAsUpEnabled = true

        setDataListItems()
        initRecyclerView()
    }

    private fun setDataListItems() {
        mDataList.add(EventModel("Item successfully delivered", "a", "11:00", "12:00", "2017-02-12"))
        mDataList.add(EventModel("Courier is out to delivery your order", "a", "11:00", "12:00", "2017-02-12"))
        mDataList.add(EventModel("Item has reached courier facility at New Delhi", "a", "11:00", "12:00", "2017-02-11"))
        mDataList.add(EventModel("Item has been given to the courier", "a", "11:00", "12:00", "2017-02-11"))
        mDataList.add(EventModel("Item is packed and will dispatch soon", "a", "11:00", "12:00", "2017-02-11"))
        mDataList.add(EventModel("Order is being readied for dispatch", "a", "11:00", "12:00", "2017-02-11"))
        mDataList.add(EventModel("Order processing initiated", "a", "11:00", "12:00", "2017-02-10"))
        mDataList.add(EventModel("Order confirmed by seller", "a", "11:00", "12:00", "2017-02-10"))
        mDataList.add(EventModel("Order placed successfully", "a", "11:00", "12:00", "2017-02-10"))
    }

    private fun initRecyclerView() {
        mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = ExampleTimeLineAdapter(mDataList)
        recyclerView.adapter = mAdapter
    }
}
