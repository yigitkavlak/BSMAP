package com.yigitkavlak.mapduty


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_layout.view.*

class RcyclerAdapter(private val taskPlaceList : ArrayList<TaskPlace>) : RecyclerView.Adapter<RcyclerAdapter.RowHolder>() {


    class RowHolder(view : View) : RecyclerView.ViewHolder(view)  {
            fun bind(taskPlace: TaskPlace){
                itemView.text_adress.text = taskPlace.address
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
            holder.bind(taskPlaceList[position])
    }

    override fun getItemCount(): Int {
            return taskPlaceList.count()
    }
}