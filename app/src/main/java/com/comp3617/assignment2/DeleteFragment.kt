package com.comp3617.assignment2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import co.metalab.asyncawait.async
import com.comp3617.assignment2.Adapter.CustomAdapter
import com.comp3617.assignment2.DB.TaskListDatabase
import com.comp3617.assignment2.Model.Task
import kotlinx.android.synthetic.main.activity_main.*

class DeleteFragment : DialogFragment(){
    private lateinit var listener : DeleteFragmentListener

    // Interface for activity to implement
    interface DeleteFragmentListener{
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    // Creates dialog and defines button actions
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ctx = activity as Context
        val builder = AlertDialog.Builder(ctx)
        builder.setMessage(R.string.confirmation)
            .setPositiveButton(R.string.yes) { _, id ->
                listener.onDialogPositiveClick(this)
            }
            .setNegativeButton(R.string.cancel) { _, id ->
                // On cancel do nothing
            }
        return builder.create()
    }

    // sets listener on attach
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as DeleteFragmentListener
    }
}