package com.roombookingapp.data.repository

import android.content.Context
import com.roombookingapp.data.RoomsServices
import com.roombookingapp.data.model.request.BookingRequest
import com.roombookingapp.data.model.response.BookingResponse
import com.roombookingapp.data.model.request.RoomListRequest
import com.roombookingapp.data.model.response.RoomDetailsResponse
import io.reactivex.Single

/**
 * Implementation class for Rooms Repository
 */
class RoomsRepositoryImpl(private val service: RoomsServices,
                          private val context: Context) : RoomsRepository {
    override fun bookTheRoom(bookingRequest: BookingRequest): Single<BookingResponse> {
        return service.bookTheRoom(bookingRequest)
    }


    override fun getRoomsList(roomListRequest: RoomListRequest): Single<List<RoomDetailsResponse>> {

        return service.getRoomsList(roomListRequest)


    }

}