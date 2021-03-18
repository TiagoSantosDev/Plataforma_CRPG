package com.plataforma.crpg.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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
import com.plataforma.crpg.ui.agenda.EventDataViewModel
import com.plataforma.crpg.ui.agenda.TimeLineAdapter
import com.plataforma.crpg.ui.agenda.TimelineAttributesBottomSheet
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BaseActivity() {

    private var mDataList = ArrayList<EventModel>()
    // private var mMealList = ArrayList<Meal>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes
    // class MealDataViewModel(application: Application) : AndroidViewModel(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithoutInject(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
/*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_agenda
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

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

        println("> call da funcao no onCreate")
        setDataListItems()
        initRecyclerView()

        action_example_activity.setOnClickListener { startActivity(Intent(this, ExampleActivity::class.java)) }

        fab_options.setOnClickListener {
            TimelineAttributesBottomSheet.showDialog(
                supportFragmentManager, mAttributes,
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
    }

    private fun setDataListItems() {

        println("> log dentro do setData")
        val eventViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(EventDataViewModel::class.java)
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
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = TimeLineAdapter(mDataList, mAttributes)
        }
    }
}

// val mealViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MealDataViewModel::class.java)

// eventDataViewModel = ViewModelProvider(this,
//        ViewModelProvider.AndroidViewModelFactory.getInstance(getAppli
//        cation()).get(EventDataViewModel.class));

// eventDataViewModel = ViewModelProvider(this).get(EventDataViewModel::class.java)
