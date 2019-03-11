package com.comp3617.assignment2


import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.comp3617.assignment2.DB.TaskListDatabase
import com.comp3617.assignment2.Model.Task
import kotlinx.android.synthetic.main.activity_add_task.*
import java.text.DateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    var db: TaskListDatabase? = null
    var listTasks: List<Task>? = null
    private lateinit var calendar: Calendar
    private lateinit var chosenDate: Date
    private var formattedDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(findViewById(R.id.add_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        calendar = Calendar.getInstance()
        listTasks = db?.getTaskDao()?.all
        db = TaskListDatabase.getInstance(this)
        // Hides 8am Reminder based on preferences
        hideReminder()

        // Listener to add a task to the database
        btn_add.setOnClickListener {
            val radioId = radio_group.checkedRadioButtonId
            println(radioId)
            if (radioId != -1) {
                val radioBtn: RadioButton = findViewById(radioId)
                Thread(Runnable {
                    val task = Task(
                        title = this.task_title.text.toString(),
                        desc = this.task_desc.text.toString(),
                        date = this.add_date.text.toString(),
                        priority = radioBtn.text.toString(),
                        reminderOn = reminder_checkbox.isChecked
                    )
                    db?.getTaskDao()?.addTask(task)
                }).start()
                finish()
            } else {
                Toast.makeText(this,
                    getString(resources.getIdentifier("choose_priority", "string", this.packageName)),
                    Toast.LENGTH_LONG).show()
            }
        } // end of btn
    }

    // Show Date Picker Dialog
    fun showDatePickerDialog(v: View) {
        val dpFragment = DatePickerFragment()
        dpFragment.show(supportFragmentManager, "TAG1")
    }

    // Listener for when date is picked from calendar
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        formattedDate = formatDate(year, month, day)
        add_date.text = formattedDate
    }

    // Formats the date
    private fun formatDate(year: Int, month: Int, day: Int): String {
        calendar.set(year, month, day, 0, 0, 0)
        chosenDate = calendar.time

        val df = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return df.format(chosenDate)
    }

    // Hides 8am Reminder depending on preferences
    private fun hideReminder() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val reminderAllowed = sharedPrefs.getBoolean("reminder", false)
        if (!reminderAllowed) {
            reminder_checkbox.visibility = View.GONE
        }
    }

    // Saves the state when the phone is rotated
    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString("date", formattedDate)
        super.onSaveInstanceState(outState)
    }

    // Restores the state upon rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        add_date.text = savedInstanceState!!.getString("date")
        formattedDate = savedInstanceState!!.getString("date")
    }
}