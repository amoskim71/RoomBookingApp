package com.roombookingapp.ui.rooms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.textChanges
import com.roombookingapp.R
import com.roombookingapp.data.model.response.RoomDetailsResponse
import com.roombookingapp.ui.adapter.RoomListAdapter
import com.roombookingapp.utils.CommonUtil
import com.roombookingapp.utils.ProgressDialog
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.booking_fragment.*
import kotlinx.android.synthetic.main.rooms_list_fragment.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList


private val TAG = RoomsListFragment::class.java.name

/**
 * class for show room list
 */
class RoomsListFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RoomsListViewModel
    private val disposable = CompositeDisposable()
    private lateinit var roomDetailsResponseList: List<RoomDetailsResponse>

    companion object {
        fun newInstance() = RoomsListFragment()
    }

    // state observer
    private val stateObserver = Observer<List<RoomDetailsResponse>> { state ->
        state?.let {

            for (room in it) {
                room.date = date.text.toString()
                room.availableSlotList = ArrayList()

                for (available in room.avail) {
                    room.availableSlotList.add(CommonUtil.convertAvailableTextToDate(available, date.text.toString()))
                }
            }
            roomDetailsResponseList = it
            val roomListAdapter = RoomListAdapter(it as MutableList<RoomDetailsResponse>)
            setAdapter(roomListAdapter)
            roomListAdapter.setDate(date.text.toString())

        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.rooms_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomsListViewModel::class.java)

        observerViewModel()

        /**
         * search function for search edit text
         */
        searchInput
                .textChanges()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe {
                    viewModel
                            .search(it.toString())
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                val diffResult = DiffUtil.calculateDiff(PostsDiffUtilCallback(viewModel.oldFilteredPosts, viewModel.filteredPosts))
                                viewModel.oldFilteredPosts.clear()
                                viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)
                                diffResult.dispatchUpdatesTo(recyclerview.adapter)
                            }.addTo(disposable)
                }.addTo(disposable)

        prevdaybtn.setOnClickListener(this)
        nextdaybtn.setOnClickListener(this)
        date.text = CommonUtil.getTheCurrentDate()

        if (CommonUtil.isNetworkStatusAvailable(this!!.context!!)) {
            viewModel.getRoomList(Date().time.toString())
            ProgressDialog.showProgressDialog(this!!.context!!)
        } else {
            Toast.makeText(context, getText(R.string.network_error), Toast.LENGTH_LONG).show()
        }



        checkbox_hour.setOnClickListener(View.OnClickListener { v ->
            val checkedView = v as CheckedTextView
            checkedView.isChecked = !checkedView.isChecked
            if (checkedView.isChecked) {
                setAdapter(RoomListAdapter(calOneHourSlotAvailableList()))

            } else {
                setAdapter(RoomListAdapter(roomDetailsResponseList as MutableList<RoomDetailsResponse>))

            }

        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.prevdaybtn -> setTheDate(-1)
            R.id.nextdaybtn -> setTheDate(1)
        }
    }

    /**
     * set the adapter to recyclerview
     */
    private fun setAdapter(roomListAdapter: RoomListAdapter) {
        recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = roomListAdapter
            ProgressDialog.dismissProgressDialog()
        }
    }


    /**
     * add the no of date tot current date
     */
    private fun setTheDate(no: Int) {
        date.text = CommonUtil.setTheDate(no, date.text.toString())
        checkbox_hour.isChecked = false
        viewModel.getRoomList(CommonUtil.getTheDateinUnixTime(date.text.toString())!!)

    }

    private fun observerViewModel() {
        viewModel.originalData.observe(this, stateObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.originalData.removeObserver(stateObserver)
    }

    /**
     * calculate one hour slot available
     */
    fun calOneHourSlotAvailableList(): ArrayList<RoomDetailsResponse> {
        var oneHourRoomsList: ArrayList<RoomDetailsResponse>
        val currentDate: Date
        val endDate: Date
        oneHourRoomsList = ArrayList()
        currentDate = Date()
        endDate = CommonUtil.addMinutesToDate(4, currentDate)
        for (item in roomDetailsResponseList) {
            for (availableSlot in item.availableSlotList) {
                if ((currentDate.equals(availableSlot.startTime) || currentDate.after(availableSlot.startTime)) && (endDate.equals(availableSlot.endtime) || endDate.before(availableSlot.endtime))) {
                    oneHourRoomsList.add(item)
                }
            }

        }
        return oneHourRoomsList
    }

}
