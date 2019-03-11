package com.comp3617.assignment2


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.comp3617.assignment2.Adapter.CustomAdapter
import com.comp3617.assignment2.DB.TaskListDatabase
import com.comp3617.assignment2.Model.Task
import kotlinx.android.synthetic.main.activity_main.*

class DisplayTaskActivity : AppCompatActivity(), DeleteFragment.DeleteFragmentListener{

    var db: TaskListDatabase? = null
    lateinit var task : Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Supports the ActionBar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Initialize DB and refresh data to populate the list with latest info
        db = TaskListDatabase.getInstance(this)
        Thread(Runnable {
            retrieveTasks()
            val tasks = db?.getTaskDao()?.all
            if (tasks!!.isEmpty()) {
            } else {
                populateTasks(tasks)
            }
        }).start()

        // Event Handler for the Row View "Clicked" by the User.  Sends user to Edit Activity
        list_view.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val intent = Intent(this@DisplayTaskActivity, EditTaskActivity::class.java)
                val task = list_view.getItemAtPosition(position) as Task
                intent.putExtra("uid", task.uid)
                intent.putExtra("title", task.title)
                intent.putExtra("desc", task.desc)
                intent.putExtra("priority", task.priority)
                intent.putExtra("date", task.date)
                intent.putExtra("reminderOn", task.reminderOn)
                startActivity(intent)
            }
        })

        // Listener for row item brings up delete dialog on long click
        list_view.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                task = list_view.getItemAtPosition(position) as Task
                showDeleteDialog()
                return true
            }
        }
    }

    // Deletes task on confirmation
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Thread(Runnable {
            db?.getTaskDao()?.delete(task)
            println("deleted: " + task)
            retrieveTasks()
        }).start()
    }

    // Show delete dialog fragment
    fun showDeleteDialog(){
        val dialog = DeleteFragment()
        dialog.show(supportFragmentManager, "TAG1")
    }

    // Refreshes ListView
    private fun retrieveTasks() {
        Thread(Runnable {
            val list = db?.getTaskDao()?.all as ArrayList<Task>
            populateTasks(list)
        }).start()
    }

    // Refreshes list on resume
    override fun onResume() {
        super.onResume()
        println("Resumed")
        retrieveTasks()

    }

    // Populates tasks
    private fun populateTasks(listTasks: List<Task>?) {
        runOnUiThread { list_view.adapter = CustomAdapter(this@DisplayTaskActivity, listTasks) }
    }

    // Events for menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Opens AddTaskActivity when Add icon is pressed
        R.id.add_task -> {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
            true
        }
        // Opens SettingsActivity when settings icon is pressed
        R.id.main_settings -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Inflates menu items in appbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

}
