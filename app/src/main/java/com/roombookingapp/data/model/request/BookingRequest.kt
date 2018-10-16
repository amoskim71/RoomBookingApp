package com.roombookingapp.data.model.request

import com.roombookingapp.data.model.Pass

/**
 * model class booking request
 */
class BookingRequest(
        val date: String,
        val timeStart: String,
        val timeEnd: String,
        val title: String,
        val description: String,
        val room: String,
        val passes: List<Pass>


)