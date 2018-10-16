package com.roombookingapp.ui.rooms

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.roombookingapp.data.model.request.RoomListRequest
import com.roombookingapp.data.model.response.RoomDetailsResponse
import com.roombookingapp.data.repository.RoomsRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private val TAG = RoomsListViewModel::class.java.name

/**
 * view model for RoomListFragment
 */
class RoomsListViewModel @Inject constructor(private val repo: RoomsRepository) : ViewModel() {

    val originalData = MutableLiveData<List<RoomDetailsResponse>>()
    val filteredPosts: MutableList<RoomDetailsResponse> = mutableListOf()
    val oldFilteredPosts: MutableList<RoomDetailsResponse> = mutableListOf()


    fun search(query: String): Completable = Completable.create {
        val wanted = oldFilteredPosts.filter {
            it.name.contains(query) || it.location.contains(query)
        }.toList()

        filteredPosts.clear()
        filteredPosts.addAll(wanted)
        it.onComplete()
    }

    fun getRoomList(text: String) {
        val request = RoomListRequest(text)
        repo.getRoomsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRoomsListReceived, this::onError)
    }

    private fun onError(error: Throwable) {
        e { "error ${error.localizedMessage}" }

    }

    private fun onRoomsListReceived(roomResponses: List<RoomDetailsResponse>) {
        d { "data received ${roomResponses.size}" }
        oldFilteredPosts.addAll(roomResponses)
        originalData.postValue(roomResponses)


    }


}
