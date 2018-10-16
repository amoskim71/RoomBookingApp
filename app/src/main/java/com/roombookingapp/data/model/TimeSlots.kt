package com.roombookingapp.data.model

import java.util.*

/**
 * model class for time slots
 */
class TimeSlots(
        val startTime: Date,
        val endTime: Date,
        var available: Boolean

)