package com.plataforma.crpg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.extentions.formatDateTime
import com.plataforma.crpg.extentions.setGone
import com.plataforma.crpg.extentions.setVisible
import com.plataforma.crpg.model.EventModel
import com.plataforma.crpg.model.Orientation
import com.plataforma.crpg.model.TimelineAttributes
import kotlinx.android.synthetic.main.item_timeline.view.*

/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineAdapter(private val mFeedList: List<EventModel>, private var mAttributes: TimelineAttributes) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        val view = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            mLayoutInflater.inflate(R.layout.item_timeline_horizontal, parent, false)
        } else {
            mLayoutInflater.inflate(R.layout.item_timeline, parent, false)
        }
        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]
        holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker_active), mAttributes.markerColor)

        if (timeLineModel.date.isNotEmpty()) {
            holder.date.setVisible()
            // holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy")
            holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd", "dd-MMM-yyyy")
        } else
            holder.date.setGone()

        if (timeLineModel.start_time.isNotEmpty()) {
            holder.start_time.setVisible()
            var newStartTime: String = timeLineModel.start_time
            newStartTime = newStartTime.substring(0, 2) + ":" + newStartTime.substring(2, 4)
            holder.start_time.text = newStartTime
        } else
            holder.start_time.setGone()

        if (timeLineModel.end_time.isNotEmpty()) {
            holder.end_time.setVisible()
            var newEndTime: String
            newEndTime = timeLineModel.end_time
            newEndTime = newEndTime.substring(0, 2) + ":" + newEndTime.substring(2, 4)
            holder.end_time.text = newEndTime
        } else
            holder.start_time.setGone()

        holder.title.text = timeLineModel.title
        holder.info.text = timeLineModel.info
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date = itemView.text_timeline_date
        val title = itemView.text_timeline_title
        val info = itemView.text_timeline_info
        val start_time = itemView.text_timeline_start_time
        val end_time = itemView.text_timeline_end_time
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
            timeline.markerSize = mAttributes.markerSize
            timeline.setMarkerColor(mAttributes.markerColor)
            timeline.isMarkerInCenter = mAttributes.markerInCenter
            timeline.markerPaddingLeft = mAttributes.markerLeftPadding
            timeline.markerPaddingTop = mAttributes.markerTopPadding
            timeline.markerPaddingRight = mAttributes.markerRightPadding
            timeline.markerPaddingBottom = mAttributes.markerBottomPadding
            timeline.linePadding = mAttributes.linePadding

            timeline.lineWidth = mAttributes.lineWidth
            timeline.setStartLineColor(mAttributes.startLineColor, viewType)
            timeline.setEndLineColor(mAttributes.endLineColor, viewType)
            timeline.lineStyle = mAttributes.lineStyle
            timeline.lineStyleDashLength = mAttributes.lineDashWidth
            timeline.lineStyleDashGap = mAttributes.lineDashGap
        }
    }
}
