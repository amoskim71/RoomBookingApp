package com.roombookingapp.data

import com.roombookingapp.data.model.request.BookingRequest
import com.roombookingapp.data.model.response.BookingResponse
import com.roombookingapp.data.model.request.RoomListRequest
import com.roombookingapp.data.model.response.RoomDetailsResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface RoomsServices {
    @Headers("Content-type: application/json")
    @POST("getrooms")
    fun getRoomsList(@Body roomListRequest: RoomListRequest): Single<List<RoomDetailsResponse>>


    @Headers("Content-type: application/json")
    @POST("sendpass")
    fun bookTheRoom(@Body response: BookingRequest): Single<BookingResponse>
}