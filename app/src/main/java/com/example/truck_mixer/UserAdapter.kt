package com.example.truck_mixer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val list: List<UserModel>,
    private val onEdit: (UserModel) -> Unit,
    private val onDelete: (UserModel) -> Unit
) : RecyclerView.Adapter<UserAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val txtUsername: TextView = v.findViewById(R.id.txtUsername)
        val txtRole: TextView = v.findViewById(R.id.txtRole)
        val txtNoTm: TextView = v.findViewById(R.id.txtNoTm)
        val btnEdit: Button = v.findViewById(R.id.btnEdit)
        val btnDelete: Button = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val u = list[pos]

        h.txtUsername.text = u.username
        h.txtRole.text = "Role: ${u.role}"
        h.txtNoTm.text = "No TM: ${u.no_tm ?: "NULL"}"


        h.btnEdit.setOnClickListener { onEdit(u) }
        h.btnDelete.setOnClickListener { onDelete(u) }
    }

    override fun getItemCount() = list.size
}
