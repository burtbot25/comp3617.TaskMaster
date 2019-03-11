package com.comp3617.assignment2

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_task.*
import java.text.DateFormat
import java.util.*

class DatePickerFragment : DialogFragment() {

    private lateinit var calendar: Calendar
    private lateinit var chosenDate: Date

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Use the current date as the default date in the picker
        calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, activity as? DatePickerDialog.OnDateSetListener, year, month, day)
    }
}