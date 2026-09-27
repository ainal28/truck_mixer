package com.example.truck_mixer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AntrianAdapter(
    private val list: List<AntrianModel>,
    private val role: String,
    private val onAction: (Int, String) -> Unit
) : RecyclerView.Adapter<AntrianAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTm: TextView = view.findViewById(R.id.txtTm)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val txtWaktu: TextView = view.findViewById(R.id.txtWaktu)
        val btnSelesai: Button = view.findViewById(R.id.btnSelesai)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_antrian, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.txtTm.text = "TM ${item.noTm}"
        holder.txtStatus.text = item.status.uppercase()
        holder.txtWaktu.text = item.waktu

        // ===== RULE TOMBOL SELESAI =====
        holder.btnSelesai.visibility = when {
            role == "super" -> View.VISIBLE
            role == "batcher" && item.status.lowercase() == "loading" -> View.VISIBLE
            else -> View.GONE
        }

        holder.btnSelesai.setOnClickListener {
            onAction(item.id, item.status)
        }
    }


    override fun getItemCount(): Int = list.size
}
