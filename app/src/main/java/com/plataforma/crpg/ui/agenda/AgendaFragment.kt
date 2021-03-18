package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.example.ExampleActivity
import com.plataforma.crpg.extentions.dpToPx
import com.plataforma.crpg.extentions.getColorCompat
import com.plataforma.crpg.extentions.setGone
import com.plataforma.crpg.extentions.setVisible
import com.plataforma.crpg.model.EventModel
import com.plataforma.crpg.model.Orientation
import com.plataforma.crpg.model.TimelineAttributes
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class AgendaFragment : Fragment() {

    private var mDataList = ArrayList<EventModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_main, container, false)

        // default values
        mAttributes = TimelineAttributes(
                markerSize = dpToPx(20f),
                markerColor = getColorCompat(R.color.material_grey_500),
                markerInCenter = true,
                markerLeftPadding = dpToPx(0f),
                markerTopPadding = dpToPx(0f),
                markerRightPadding = dpToPx(0f),
                markerBottomPadding = dpToPx(0f),
                linePadding = dpToPx(2f),
                startLineColor = getColorCompat(R.color.colorAccent),
                endLineColor = getColorCompat(R.color.colorAccent),
                lineStyle = TimelineView.LineStyle.NORMAL,
                lineWidth = dpToPx(2f),
                lineDashWidth = dpToPx(4f),
                lineDashGap = dpToPx(2f)
        )

        setDataListItems()
        initRecyclerView()

        action_example_activity.setOnClickListener {
            val intent = Intent(activity, ExampleActivity::class.java)
            startActivity(intent)
        }

        fab_options.setOnClickListener {
            TimelineAttributesBottomSheet.showDialog(
                    parentFragmentManager, mAttributes,
                    object : TimelineAttributesBottomSheet.Callbacks {
                        override fun onAttributesChanged(attributes: TimelineAttributes) {
                            mAttributes = attributes
                            initAdapter()
                        }
                    }
            )
        }

        mAttributes.onOrientationChanged = { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView()
        }

        mAttributes.orientation = Orientation.VERTICAL


        return root
    }

    private fun setDataListItems() {

        println("> log dentro do setData")
        val eventViewModel = ViewModelProvider(this).get(EventDataViewModel::class.java)
        mDataList = eventViewModel.getEventCollectionFromJSON()
        // guarantee that all events are sorted by their starting time
        mDataList.sortBy { it.start_time }
        println("Main activity data list index 0: " + mDataList.get(0))
        println("Main activity data list index 1: " + mDataList.get(1))
    }

    private fun initRecyclerView() {
        initAdapter()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.getChildAt(0).top < 0) dropshadow.setVisible() else dropshadow.setGone()
            }
        })
    }

    private fun initAdapter() {
        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = TimeLineAdapter(mDataList, mAttributes)
        }
    }



}
