package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
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
import com.plataforma.crpg.extentions.dpToPx
import com.plataforma.crpg.extentions.getColorCompat
import com.plataforma.crpg.extentions.setGone
import com.plataforma.crpg.extentions.setVisible
import com.plataforma.crpg.model.EventModel
import com.plataforma.crpg.model.Orientation
import com.plataforma.crpg.model.TimelineAttributes
import kotlinx.android.synthetic.main.fragment_agenda.*
import java.util.ArrayList

class AgendaFragment : Fragment() {

    private var mDataList = ArrayList<EventModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_agenda, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        //esta a ter problemas aqui
        initRecyclerView()

        println("passou do recycler view")

        /*
        action_example_activity.setOnClickListener {
            println(">Activity: " + activity)
            val intent = Intent(activity, ExampleActivity::class.java)
            startActivity(intent)
        }*/
/*
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
*/
        mAttributes.onOrientationChanged = { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView()
        }

        mAttributes.orientation = Orientation.VERTICAL


    }

    private fun setDataListItems() {

        println("> log dentro do setData")
        val eventViewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)
        mDataList = eventViewModel.getEventCollectionFromJSON()
        // guarantee that all events are sorted by their starting time
        mDataList.sortBy { it.start_time }
    }

    private fun initRecyclerView() {
        initAdapter()
        println(">Passou do init Adapter")
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

        println("Activity:" + activity)
        println("Recycler View:" + recyclerView)

        recyclerView.apply {
            layoutManager = mLayoutManager
            println(">entrou no apply")
            adapter = TimeLineAdapter(mDataList, mAttributes)
        }
    }
}