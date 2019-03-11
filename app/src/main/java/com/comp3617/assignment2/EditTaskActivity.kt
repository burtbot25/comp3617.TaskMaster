package com.comp3617.assignment2

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import com.comp3617.assignment2.DB.TaskListDatabase
import com.comp3617.assignment2.Model.Task
import kotlinx.android.synthetic.main.activity_edit_task.*
import java.text.DateFormat
import java.util.*
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.preference.PreferenceManager



class EditTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    DeleteFragment.DeleteFragmentListener {

    var db: TaskListDatabase? = null
    var uid: Long = 0
    lateinit var title: String
    lateinit var desc: String
    lateinit var date: String
    lateinit var priority: String
    var reminderOn: Boolean = false
    private lateinit var calendar: Calendar
    private lateinit var chosenDate: Date
    lateinit var task: Task
    var formattedDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        setSupportActionBar(findViewById(R.id.edit_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        calendar = Calendar.getInstance()
        db = TaskListDatabase.getInstance(this)

        Thread(Runnable {
            task = db?.getTaskDao()?.findById(uid) as Task
        }).start()

        // Get data from the DisplayTaskActivity
        uid = intent.getLongExtra("uid", 0)
        title = intent.getStringExtra("title")
        desc = intent.getStringExtra("desc")
        date = intent.getStringExtra("date")
        formattedDate = intent.getStringExtra("date")
        priority = intent.getStringExtra("priority")
        reminderOn = intent.getBooleanExtra("reminderOn", false)
        setViewValues()

        // Button to Update Task
        btn_update.setOnClickListener {
            Thread(Runnable {
                val radioId = edit_radio_group.checkedRadioButtonId
                val radioBtn: RadioButton = findViewById(radioId)
                task.title = edit_title.text.toString()
                task.desc = edit_desc.text.toString()
                task.date = edit_date.text.toString()
                task.priority = radioBtn.text.toString()
                task.reminderOn = edit_reminder_checkbox.isChecked
                db?.getTaskDao()?.update(task)
                println("updated: " + task)
            }).start()
            finish()
        }

        //Button to Delete Task
        btn_delete.setOnClickListener {
            showDeleteDialog()
        }
    }

    // Deletes task when confirmed
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Thread(Runnable {
            task = db?.getTaskDao()?.findById(uid) as Task
            db?.getTaskDao()?.delete(task)
            println("deleted: " + task)
        }).start()
        finish()
    }

    // Show delete dialog
    private fun showDeleteDialog() {
        val dialog = DeleteFragment()
        dialog.show(supportFragmentManager, "TAG1")
    }

    // Sets view fields with values from the task selected
    private fun setViewValues() {
        edit_title.setText(title)
        edit_desc.setText(desc)
        edit_date.text = date
        setPriority()
        setReminder()
    }

    // Show date picker
    fun showDatePickerDialog(v: View) {
        val dpFragment = DatePickerFragment()
        dpFragment.show(supportFragmentManager, "TAG2")
    }

    // Handler when date is selected. Sets Date
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        formattedDate = formatDate(year, month, day)
        edit_date.text = formattedDate
    }

    // Formats date
    private fun formatDate(year: Int, month: Int, day: Int): String {
        calendar.set(year, month, day, 0, 0, 0)
        chosenDate = calendar.time

        val df = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return df.format(chosenDate)
    }

    // Saves the state when the phone is rotated
    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString("date", formattedDate)
        super.onSaveInstanceState(outState)
    }

    // Restores the state upon rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        edit_date.text = savedInstanceState!!.getString("date")
        formattedDate = savedInstanceState!!.getString("date")
    }

    // Toggles the reminder based on task's value
    private fun setReminder() {
        if (reminderOn) {
            edit_reminder_checkbox.toggle()
        }
    }

    // Sets priority checkbox based on task's value
    private fun setPriority() {
        if (priority == edit_radio_low.text) {
            edit_radio_low.toggle()
        } else if (priority == edit_radio_med.text) {
            edit_radio_med.toggle()
        } else if (priority == edit_radio_high.text) {
            edit_radio_high.toggle()
        }
    }

    // Handler for menu item
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Shares task selected via email application on mobile device
        R.id.share_task -> {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "abc@gmail.com", null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Task")
            emailIntent.putExtra(Intent.EXTRA_TEXT, task.toString())
            startActivity(Intent.createChooser(emailIntent, ""))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Determines if the share icon or the 8am reminder checkbox should appear
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val shareAllowed = sharedPrefs.getBoolean("email_reminder", false)
        val reminderAllowed = sharedPrefs.getBoolean("reminder", false)

        // Inflate depending on settings
        if (shareAllowed) {
            menuInflater.inflate(R.menu.edit_task_menu, menu)
        }

        if (!reminderAllowed) {
            edit_reminder_checkbox.visibility = View.GONE
        }
        return true
    }
}
