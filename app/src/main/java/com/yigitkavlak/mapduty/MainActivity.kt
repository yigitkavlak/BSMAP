package com.yigitkavlak.mapduty

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var taskPlaceArray = ArrayList<TaskPlace>()
    private var rcyclerAdapter: RcyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rcyclerView.layoutManager = layoutManager

        try {
            val database = openOrCreateDatabase("TaskPlaces", Context.MODE_PRIVATE, null)
            val cursor = database.rawQuery("SELECT * FROM places", null)

            val addressIndex = cursor.getColumnIndex("address")
            val latitudeIndex = cursor.getColumnIndex("latitude")
            val longitudeIndex = cursor.getColumnIndex("longitude")


            while (cursor.moveToNext()) {
                val addressFromDB = cursor.getString(addressIndex)
                val latitudeFromDB = cursor.getDouble(latitudeIndex)
                val longitudeFromDB = cursor.getDouble(longitudeIndex)


                val myTaskPlace = TaskPlace(addressFromDB, latitudeFromDB, longitudeFromDB)
                taskPlaceArray.add(myTaskPlace)


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rcyclerAdapter = RcyclerAdapter(taskPlaceArray)
        rcyclerView.adapter = rcyclerAdapter


    }
}