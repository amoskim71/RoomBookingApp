package com.roombookingapp.ui.rooms

import android.support.v7.util.DiffUtil
import com.roombookingapp.data.model.response.RoomDetailsResponse

class PostsDiffUtilCallback(private val oldList: List<RoomDetailsResponse>, private val newList: List<RoomDetailsResponse>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name .contentEquals( newList[newItemPosition].name)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}