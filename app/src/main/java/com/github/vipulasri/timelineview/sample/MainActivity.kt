package com.github.vipulasri.timelineview.sample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.github.vipulasri.timelineview.sample.example.ExampleActivity
import com.github.vipulasri.timelineview.sample.extentions.dpToPx
import com.github.vipulasri.timelineview.sample.extentions.getColorCompat
import com.github.vipulasri.timelineview.sample.extentions.setGone
import com.github.vipulasri.timelineview.sample.extentions.setVisible
import com.github.vipulasri.timelineview.sample.model.EventModel
import com.github.vipulasri.timelineview.sample.model.Meal
import com.github.vipulasri.timelineview.sample.model.Orientation
import com.github.vipulasri.timelineview.sample.model.TimelineAttributes
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BaseActivity() {

    private var mDataList = ArrayList<EventModel>()
    private var mMealList = ArrayList<Meal>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes


    //class MealDataViewModel(application: Application) : AndroidViewModel(application)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithoutInject(R.layout.activity_main)
        //val mealViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MealDataViewModel::class.java)

        // eventDataViewModel = ViewModelProvider(this,
        //        ViewModelProvider.AndroidViewModelFactory.getInstance(getAppli
        //        cation()).get(EventDataViewModel.class));

        // eventDataViewModel = ViewModelProvider(this).get(EventDataViewModel::class.java)

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

        // val eventP = JSONEventParser()
        // mDataList = eventDataViewModel.setDataListItems()
        //println(mDataList)
        //val mealViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MealDataViewModel::class.java)
        //val test = ViewModelProvider.AndroidViewModelFactory(application).create(Test::class.java)
        //test.b()
        //mealViewModel.testJSON()
        //mealViewModel.testJSON_Extract()
        //mealViewModel.convertMealsToJSON()

        val eventViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(EventDataViewModel::class.java)
        mDataList = eventViewModel.getEventCollectionFromJSON()
        //mMealList = eventViewModel.getMealFromJson()
        println("Main activity data list index 0: " + mDataList.get(0))





    /*
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-12 08:00"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-12 08:00"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-11 21:00"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-11 18:00"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-11 09:30"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-11 08:00"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-10 15:00"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-10 14:30"))
        mDataList.add(EventModel("evento1", "a", "11:00", "12:00", "2017-02-10 14:00"))

     */
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
