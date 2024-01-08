package com.example.myapplication

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class ReservationAdapter(private val reservedList: List<String>) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservedusername = itemView.findViewById<TextView>(R.id.reservedUserName)
        val reservedorder = itemView.findViewById<TextView>(R.id.reservedOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reserved_item, parent, false)
        return ReservationViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val currentUser = reservedList[position]
        holder.reservedorder.text = "${position+1}"
        holder.reservedusername.text = currentUser
    }

    override fun getItemCount(): Int {
        return reservedList.size
    }
}