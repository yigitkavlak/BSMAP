package com.yigitkavlak.mapduty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_task.*


class TaskFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        val saveButton = view.findViewById<View>(R.id.saveButton) as Button
        val cancelButton = view.findViewById<View>(R.id.cancelButton) as Button

        saveButton.setOnClickListener {
            Toast.makeText(context!!, " Save Button Clicked", Toast.LENGTH_LONG).show()
        }

        cancelButton.setOnClickListener {
            Toast.makeText(context!!, " Cancel Button Clicked", Toast.LENGTH_LONG).show()
            /* if (addTaskbutton.visibility == View.INVISIBLE) {
                 addTaskbutton.visibility = View.VISIBLE
             }

             */


        }



        return view
    }


}