package com.roombookingapp.ui.roombook

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.widget.Toast
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.roombookingapp.R
import com.roombookingapp.data.model.Pass
import com.roombookingapp.data.model.request.BookingRequest
import com.roombookingapp.data.model.response.BookingResponse
import com.roombookingapp.data.model.response.RoomDetailsResponse
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.booking_fragment.*
import java.util.*
import javax.inject.Inject
import com.roombookingapp.utils.CommonUtil
import com.roombookingapp.utils.ProgressDialog
import com.roombookingapp.utils.SeekBarListener
import kotlinx.android.synthetic.main.rooms_list_fragment.*


private val TAG = BookRoomFragment::class.java.name

/**
 * class for handle the booking a room screen and feature
 */
class BookRoomFragment : Fragment(), View.OnClickListener, SeekBarListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BookRoomViewModel
    var passList = ArrayList<Pass>()
    lateinit var roomDetailsResponse: RoomDetailsResponse
    lateinit var endSeekDate: Date
    lateinit var startSeekDate: Date

    companion object {
        fun newInstance() = BookRoomFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.booking_fragment, container, false)

        roomDetailsResponse = arguments!!.getSerializable(RoomDetailsResponse::class.java.name) as RoomDetailsResponse
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BookRoomViewModel::class.java)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerBarLayout.setTimeSlotListener(this)
        room_no.text = roomDetailsResponse.name
        timerBarLayout.setTheCurrentDate(roomDetailsResponse.date)
        timerBarLayout.setTheAvailableDetails(roomDetailsResponse.availableSlotList)
        addperson.setOnClickListener(this)
        add_button.setOnClickListener(this)
        hide_button.setOnClickListener(this)
        sendPassButton.setOnClickListener(this)
        observerViewModel()
        toolbarLayout.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addperson -> add_person_layout.visibility = View.VISIBLE
            R.id.add_button -> addPersonDetaisl()
            R.id.sendPassButton -> sendTheBookDetails()
            R.id.hide_button -> add_person_layout.visibility = View.GONE
        }

    }

    override fun onTimeSlotChange(seekDate: Date) {
        startSeekDate = seekDate
        if (seekDate != null) {
            endSeekDate = CommonUtil.addMinutesToDate(1, seekDate)
            slot_time.text = CommonUtil.getHoursMinutes(seekDate) + "-" + CommonUtil.getHoursMinutes(endSeekDate)
        }
    }

    /**
     * Api call for  Sending the BookingRequest details
     */
    private fun sendTheBookDetails() {
        if (validation()) {
            val booking = BookingRequest(CommonUtil.getTheDateinUnixTime(roomDetailsResponse.date).toString(), startSeekDate.time.toString(),
                    endSeekDate.time.toString(), title.text.toString(), description.text.toString(), roomDetailsResponse.name, passList)

            if (timerBarLayout.checkTheSlotBooked(startSeekDate, endSeekDate)) {


                if (CommonUtil.isNetworkStatusAvailable(this!!.context!!)) {

                    viewModel.bookTheDetails(booking)
                    ProgressDialog.showProgressDialog(this!!.context!!)
                    clearMainTheFields()
                } else {
                    Toast.makeText(context, getText(R.string.network_error), Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, getText(R.string.slot_not_availble), Toast.LENGTH_LONG).show()
            }
        }

    }

    /**
     * validation for title and description
     */

    private fun validation(): Boolean {
        var status = true
        if (TextUtils.isEmpty(title.text.toString())) {
            status = false
            title.error = getString(R.string.title_error_text)
        }


        if (CommonUtil.isValidEmail(description.text.toString())) {
            status = false
            description.error = getString(R.string.description_error_text)
        }

        return status
    }

    /**
     * adding the name, email, pphone details to Person object
     */
    private fun addPersonDetaisl() {
        if (checkValidation()) {
            person_layout.visibility = View.VISIBLE
            val pass = Pass(personName.text.toString(), personEmail.text.toString(), personPhone.text.toString())
            passList.add(pass)
            val textView = TextView(context)
            textView.text = pass.name
            person_layout.addView(textView)
            clearTheFields()
        }

    }

    /**
     * clear the name, email,phone fields
     */
    private fun clearTheFields() {
        personName.setText("")
        personEmail.setText("")
        personPhone.setText("")
    }

    /**
     * clear the title, description
     */
    private fun clearMainTheFields() {
        title.setText("")
        description.setText("")
    }

    /**
     * validation for name , email, description
     */
    private fun checkValidation(): Boolean {
        var status = true
        if (TextUtils.isEmpty(personName.text.toString())) {
            status = false
            personName.error = getString(R.string.person_name_error_text)
        }


        if (!CommonUtil.isValidEmail(personEmail.text.toString())) {
            status = false
            personEmail.error = getString(R.string.person_email_error_text)
        }

        if (!CommonUtil.isValidPhoneNumber(personPhone.text.toString())) {
            status = false
            personPhone.error = getString(R.string.person_phone_error_text)
        }


        return status
    }


    private val stateObserver = Observer<BookingResponse> {
        ProgressDialog.dismissProgressDialog()
        Log.d(TAG, "data -> ${it!!.success} ")
        if (it.success) {
            Toast.makeText(context, getText(R.string.room_boooked), Toast.LENGTH_LONG).show()
            fragmentManager!!.popBackStack()

        } else {
            Toast.makeText(context, getText(R.string.room_not_boooked), Toast.LENGTH_LONG).show()
        }

    }

    private fun observerViewModel() {
        viewModel.stateLiveData.observe(this, stateObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateLiveData.removeObserver(stateObserver)
    }


}
