package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WasherAdapter(private val washerList: List<Washer>) : RecyclerView.Adapter<WasherAdapter.WasherViewHolder>() {

    class WasherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val washerName: TextView = itemView.findViewById(R.id.txtWasherName)
        val btnAction: Button = itemView.findViewById(R.id.btnAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return WasherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WasherViewHolder, position: Int) {
        val currentWasher = washerList[position]
        holder.washerName.text = currentWasher.washerName

        holder.btnAction.text = when (currentWasher.washerState) {
            "USED" -> "Used"
            "AVAILABLE" -> "Available"
            else -> "Broken"
        }

        holder.btnAction.setOnClickListener {
            // 여기에 버튼 클릭 시 동작을 정의합니다.
            // 예를 들어, 해당 Dryer에 대한 작업을 실행하거나 특정 기능을 수행할 수 있습니다.
        }
    }

    override fun getItemCount(): Int {
        return washerList.size
    }
}