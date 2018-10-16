package com.roombookingapp.ui.custom

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import com.roombookingapp.R
import com.roombookingapp.utils.SeekBarListener
import com.roombookingapp.data.model.AvailableSlots
import com.roombookingapp.data.model.TimeSlots
import com.roombookingapp.utils.CommonUtil
import kotlinx.android.synthetic.main.timerbar_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimerBarLayout : LinearLayout {

    private val finalTimeSlotsList = ArrayList<TimeSlots>()
    private var availableSlotList = ArrayList<AvailableSlots>()
    private lateinit var seekBarTimeSlotChangeListener: SeekBarListener
    private var presentDate = CommonUtil.convertIntoDateFormat(CommonUtil.getTheCurrentDate() + " " + "07:00")

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.timerbar_view, this, true)
        getTimeBarDetails()
        setTheDatatoViews()

    }

    private fun setTheDatatoViews() {
        setTheHourSlots()
    }

    fun setTheAvailableDetails(availableList: ArrayList<AvailableSlots>) {
        availableSlotList = availableList
        initDataToSeekbar()
        for (finalValue in finalTimeSlotsList) {
            checkTheSlotBooked(finalValue)
        }
    }

    /**
     * set the Hours slots to seekbar top
     */
    private fun setTheHourSlots() {
        for (i in 7..19) {
            val hourTextView = TextView(context)
            hourTextView.text = i.toString()
            hourTextView.setTextColor(resources.getColor(R.color.red))
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1.0f;
            if (i != 19)
                hourTextView.setLayoutParams(layoutParams);
            timeslotsLayout.addView(hourTextView)
        }
    }

    /**
     * Intailizing seekbar
     */
    private fun initDataToSeekbar() {


        seekBar!!.initData(finalTimeSlotsList!!)
        seekBar!!.invalidate()
        seekBarTimeSlotChangeListener.onTimeSlotChange(presentDate)
        seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar

                if ((i % 2.083).toInt() == 0) {
                    val quotient = i / 2.083
                    val seekDate = CommonUtil.addMinutesToDate(quotient.toInt(), presentDate)

                    val p = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT)
                    p.addRule(RelativeLayout.BELOW, seekBar.id)
                    val thumbRect = seekBar!!.thumb.bounds
                    p.setMargins(
                            thumbRect.centerX(), 0, 0, 0)
                    progress_text.setLayoutParams(p)

                    progress_text!!.text = CommonUtil.getHoursMinutes(seekDate)
                    seekBarTimeSlotChangeListener.onTimeSlotChange(seekDate)
                }


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    /**
     * check the slot available
     */
    fun checkTheSlotBooked(finalValue: TimeSlots) {
        for (item in availableSlotList) {
            if ((finalValue.startTime.equals(item.startTime) || finalValue.startTime.after(item.startTime)) && (finalValue.endTime.equals(item.endtime) || finalValue.endTime.before(item.endtime))) {
                finalValue.available = true
            }
        }
    }

    /**
     * check the slot available
     */
    fun checkTheSlotBooked(startDate: Date, endDate: Date): Boolean {
        var status = false
        for (item in availableSlotList) {
            if ((startDate.equals(item.startTime) || startDate.after(item.startTime)) && (endDate.equals(item.endtime) || endDate.before(item.endtime))) {
                status = true
            }
        }
        return status
    }


    fun getTimeBarDetails() {
        var startDate = presentDate
        for (i in 0..47) {
            val endDate = CommonUtil.addMinutesToDate(1, startDate)
            val timeSlot = TimeSlots(startDate, endDate, false)
            finalTimeSlotsList.add(timeSlot)
            startDate = endDate
        }

    }


    fun setTimeSlotListener(seekBarListener: SeekBarListener) {
        seekBarTimeSlotChangeListener = seekBarListener
    }

    fun setTheCurrentDate(text: String) {
        presentDate = CommonUtil.convertIntoDateFormat(text + " " + "07:00")

    }

}