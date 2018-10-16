package com.roombookingapp.data.repository

import com.roombookingapp.data.model.request.BookingRequest
import com.roombookingapp.data.model.response.BookingResponse
import com.roombookingapp.data.model.request.RoomListRequest
import com.roombookingapp.data.model.response.RoomDetailsResponse
import io.reactivex.Single

interface RoomsRepository {
    fun getRoomsList(roomListRequest: RoomListRequest): Single<List<RoomDetailsResponse>>
    fun bookTheRoom(bookingRequest: BookingRequest): Single<BookingResponse>
}