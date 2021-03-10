package com.github.vipulasri.timelineview.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.github.vipulasri.timelineview.sample.example.ExampleActivity
import com.github.vipulasri.timelineview.sample.extentions.dpToPx
import com.github.vipulasri.timelineview.sample.extentions.getColorCompat
import com.github.vipulasri.timelineview.sample.extentions.setGone
import com.github.vipulasri.timelineview.sample.extentions.setVisible
import com.github.vipulasri.timelineview.sample.model.OrderStatus
import com.github.vipulasri.timelineview.sample.model.Orientation
import com.github.vipulasri.timelineview.sample.model.TimeLineModel
import com.github.vipulasri.timelineview.sample.model.TimelineAttributes
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class DatePickerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithoutInject(R.layout.activity_date_picker)

        val button = findViewById<Button>(R.id.button_escolher)
        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


}
