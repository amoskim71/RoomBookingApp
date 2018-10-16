package com.roombookingapp.ui.roombook

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.roombookingapp.data.model.request.BookingRequest
import com.roombookingapp.data.repository.RoomsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.roombookingapp.data.model.response.BookingResponse
/**
 * view mode for bookRoomFragment
 */
class BookRoomViewModel @Inject constructor(private val repo: RoomsRepository) : ViewModel() {

    val stateLiveData = MutableLiveData<BookingResponse>()

    fun bookTheDetails(bookingRequest: BookingRequest) {

        repo.bookTheRoom(bookingRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBookingResponseRecieved, this::onError)
    }

    private fun onError(error: Throwable) {
        e { "error ${error.localizedMessage}" }
        //originalData.value = ErrorState(error.localizedMessage, obtainCurrentData(), false)
    }

    private fun onBookingResponseRecieved(bookingResponse: BookingResponse) {
        d { "data received ${bookingResponse.success}" }
        stateLiveData.value = bookingResponse

    }

}
