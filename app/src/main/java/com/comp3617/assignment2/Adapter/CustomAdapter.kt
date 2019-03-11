package com.comp3617.assignment2.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat.getColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.comp3617.assignment2.Model.Task
import com.comp3617.assignment2.R

class CustomAdapter(private val ctx: Context, private val listTask : List<Task>?) : ArrayAdapter<Task>(ctx, 0, listTask) {
    lateinit var task : Task
    lateinit var row_layout : RelativeLayout
    lateinit var row_title : TextView
    lateinit var row_desc : TextView
    lateinit var due_date : TextView



    // Gets the view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rowView : View? = null

        // Inflate view
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.row_layout, parent, false)
        } else {
            rowView = convertView
        }

        // Retrieve View IDs
        row_layout = rowView!!.findViewById<TextView>(R.id.relative_layout) as RelativeLayout
        row_title = rowView.findViewById<TextView>(R.id.row_title)
        row_desc = rowView.findViewById<TextView>(R.id.row_desc)
        due_date = rowView.findViewById<TextView>(R.id.due_date)

        // Retrieve task
        task = listTask!![position]

        // Sets View values
        setViewValues()

        return rowView
    }

    // Sets the values of the views
    private fun setViewValues(){
        row_title.text = task.title
        row_desc.text = task.desc
        due_date.text = task.date
        setBackgroundColor(row_layout)
    }

    // Sets background of the layout for the task based on priority
    private fun setBackgroundColor(row_layout : RelativeLayout){
        val blue = ContextCompat.getColor(context, R.color.Blue)
        val yellow = ContextCompat.getColor(context, R.color.Yellow)
        val red = ContextCompat.getColor(context, R.color.Red)


        if (task.priority == "Low"){
            row_layout.setBackgroundColor(blue)
        } else if (task.priority == "Medium"){
            row_layout.setBackgroundColor(yellow)
        } else if (task.priority == "High"){
            row_layout.setBackgroundColor(red)
        }
    }

}