package com.eugene.signdetect.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eugene.signdetect.R
import com.eugene.signdetect.domain.entity.DetectionHistory
import com.eugene.signdetect.domain.entity.DetectionResult
import com.eugene.signdetect.presentation.util.SignName
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items: List<DetectionHistory> = emptyList()

    fun submitList(newItems: List<DetectionHistory>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val resultTextView: TextView = itemView.findViewById(R.id.resultTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(item: DetectionHistory) {
            val labelNames = item.label
                .split(",")
                .map { it.trim() }
                .map { SignName.getName(it) }
                .joinToString(", ")

            resultTextView.text = labelNames

            val formattedDate = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                .format(item.time)
            timestampTextView.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detection, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}