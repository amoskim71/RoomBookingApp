package com.roombookingapp.data.model.response

import com.roombookingapp.data.model.AvailableSlots
import java.io.Serializable

/**
 * model class for RoomDetailsResponse
 */
data class RoomDetailsResponse(
        val name: String,
        val location: String,
        val size: String,
        var date: String,
        val capacity: Int,
        val images: List<String>,
        val avail: List<String>,
        val equipment: List<String>,
        var availableSlotList: ArrayList<AvailableSlots>
) : Serializable