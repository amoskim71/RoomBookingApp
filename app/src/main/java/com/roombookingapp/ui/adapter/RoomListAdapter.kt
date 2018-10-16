package com.roombookingapp.ui.adapter

import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.roombookingapp.R
import com.roombookingapp.RoomBookingApp
import com.roombookingapp.data.model.response.RoomDetailsResponse
import com.roombookingapp.di.GlideApp
import com.roombookingapp.ui.home.BaseActivity
import com.roombookingapp.ui.roombook.BookRoomFragment
import com.roombookingapp.utils.Constants
import com.roombookingapp.utils.SeekBarListener
import kotlinx.android.synthetic.main.booking_fragment.*
import kotlinx.android.synthetic.main.item_room.view.*
import kotlinx.android.synthetic.main.rooms_list_fragment.*
import java.util.*

/**
 * Adapter class for Room list
 */
class RoomListAdapter(roomListResponse: MutableList<RoomDetailsResponse>) : RecyclerView.Adapter<RoomListAdapter.ViewHolder>(), SeekBarListener {

    private val roomResponses: MutableList<RoomDetailsResponse>

    private var date: String = ""

    init {
        roomResponses = roomListResponse
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_room, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = roomResponses.get(position)
        holder.name.text = item.name
        holder.location.text = item.location
        holder.size.text = item.size
        holder.capacity.text = item.capacity.toString()
        holder.timerBarLayout.setTheCurrentDate(item.date)
        holder.timerBarLayout.setTimeSlotListener(this)
        for (available in item.avail) {
            val textView = TextView(holder.name.context)
            TextViewCompat.setTextAppearance(textView, R.style.AppTheme_Widget_TextView);
            textView.text = available
            holder.availabilityLayout.addView(textView)
        }

        for (equipment in item.equipment) {
            val textView = TextView(holder.name.context)
            TextViewCompat.setTextAppearance(textView, R.style.AppTheme_Widget_TextView);
            textView.text = equipment
            holder.equipmentLayout.addView(textView)
        }

        GlideApp.with(holder.itemView.context)
                .load(Constants.API_BASE_URL + item.images.get(0))
                .into(holder.image)
        //Sub layout
        holder.bookButton.setOnClickListener(View.OnClickListener { v ->
            val bundle = Bundle()
            item.date = date
            bundle.putSerializable(RoomDetailsResponse::class.java.name, item)
            val bookRoomFragment = BookRoomFragment.newInstance()
            bookRoomFragment.arguments = bundle
            ((RoomBookingApp.activity) as BaseActivity).replaceTheFragment(bookRoomFragment, BookRoomFragment::class.java.name)
        })


        holder.timerBarLayout.setTheAvailableDetails(item.availableSlotList)

        holder.container.setOnClickListener(View.OnClickListener { v ->
            holder.subContainerLayout.visibility = View.VISIBLE
            holder.timerBarLayout.visibility = View.VISIBLE
        })
    }

    override fun getItemCount(): Int {
        return roomResponses.size
    }

    fun setDate(dateText: String) {
        date = dateText
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var container = view.container
        var name = view.name
        var location = view.location
        var size = view.size
        var capacity = view.capacity
        var image = view.image
        var subContainerLayout = view.sub_container_layout
        var equipmentLayout = view.equipment_layout
        var availabilityLayout = view.availability_layout
        var timerBarLayout = view.timerBarLayout
        var bookButton = view.book
    }

    override fun onTimeSlotChange(time: Date) {

    }
}