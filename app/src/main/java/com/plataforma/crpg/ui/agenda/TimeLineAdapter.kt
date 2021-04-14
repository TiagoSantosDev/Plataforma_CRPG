package com.plataforma.crpg.ui.agenda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.extentions.formatDateTime
import com.plataforma.crpg.extentions.setGone
import com.plataforma.crpg.extentions.setVisible
import com.plataforma.crpg.model.*
import com.plataforma.crpg.ui.meals.MealsFragment
import com.plataforma.crpg.ui.transports.TransportsFragment
import kotlinx.android.synthetic.main.item_timeline.view.*


/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineAdapter(private val mFeedList: List<Event>, private var mAttributes: TimelineAttributes, val ctx: Context) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var currentDateMealOrder = MealOrder("14042021", 0, pedido_carne = false, pedido_peixe = false, pedido_dieta = false, pedido_vegetariano = false)
    lateinit var currentDateMeal: Meal

    private lateinit var mLayoutInflater: LayoutInflater
    //val Context mContext = getActivity();

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

        //onClick on a card open pop up or go to Meal or Transport Fragment
        holder.itemView.setOnClickListener {
            val id: String = mFeedList[position].title
            val tipo: EventType = mFeedList[position].type
            println("ID do cartao: $id")
            println("Tipo do cartao: $tipo")

            when(tipo){
                EventType.ACTIVITY ->
                    println("Entrou na actividade") //Toast.makeText(context, "Actividade", Toast.LENGTH_SHORT).show()

                EventType.TRANSPORTS -> {

                    //Toast.makeText(context, "Transports", Toast.LENGTH_SHORT).show()
                    val fragment: Fragment = TransportsFragment()
                    val fragmentManager: FragmentManager = (ctx as AppCompatActivity).supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    println("Fragment Manager: " + fragmentManager.toString())
                    println("Fragment Transaction: " + fragmentTransaction.toString())
                    fragmentTransaction?.replace(R.id.nav_host_fragment, fragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()

                }

                EventType.MEAL -> {

                    //Toast.makeText(context, "Transports", Toast.LENGTH_SHORT).show()
                    val fragment: Fragment = MealsFragment()
                    //val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentManager: FragmentManager = (ctx as AppCompatActivity).supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                }

            }
        }

        //se almoço nao estiver escolhido
        //card informa que pode ser clicado para escolher almoço
        if (timeLineModel.title.isNotEmpty() && currentDateMealOrder.pedido_feito == 0) {
            when(timeLineModel.title){
                "ALMOÇO" -> {
                    holder.title.text = timeLineModel.title
                    holder.info.text = "Clicar para selecionar almoço!"
                }
                "JANTAR" -> {
                    holder.title.text = timeLineModel.title
                    holder.info.text = "Clicar para selecionar jantar!"
                }
                "TRANSPORTE" -> {
                    holder.title.text = timeLineModel.title
                    holder.info.text = "Clicar para horários dos transportes!"
                }
                else -> {holder.title.text = timeLineModel.title
                holder.info.text = timeLineModel.info}
            }
        }else if(timeLineModel.title.isNotEmpty() && currentDateMealOrder.pedido_feito != 0){
            when(currentDateMealOrder.pedido_feito){
                //1-> holder.info.text = currentDateMeal.prato_carne
                //2-> holder.info.text = currentDateMeal.prato_peixe
                //3-> holder.info.text = currentDateMeal.prato_dieta
                //4-> holder.info.text = currentDateMeal.prato_vegetariano
            }




        }
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
//val fragmentManager: FragmentManager? = (context as MainActivity?)?.supportFragmentManager
//val fragmentManager: FragmentManager? = (context as MainActivity?)?.supportFragmentManager
// val fragmentManager: FragmentManager? = (context as MainActivity?)?.supportFragmentManager
// private val contextMeal: MealsFragment.Companion = MealsFragment
////private val contextTransport: TransportsFragment.Companion = TransportsFragment