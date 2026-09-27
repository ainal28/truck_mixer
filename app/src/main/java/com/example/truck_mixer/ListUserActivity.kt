package com.example.truck_mixer

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.privacysandbox.tools.core.model.Method
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class ListUserActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private val list = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        rv = findViewById(R.id.rvUser)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<FloatingActionButton>(R.id.fabAddUser)
            .setOnClickListener {
                startActivity(Intent(this, AddUserActivity::class.java))
            }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val url = "http://10.0.2.2:3000/users"

        val req = StringRequest(
            Request.Method.GET, url,
            { res ->
                list.clear()
                val arr = JSONArray(res)

                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(
                        UserModel(
                            o.getInt("id"),
                            o.getString("username"),
                            o.getString("role"),
                            o.optString("no_tm", null)
                        )
                    )
                }

                rv.adapter = UserAdapter(
                    list,
                    onEdit = { user ->
                        val i = Intent(this, EditUserActivity::class.java)
                        i.putExtra("id", user.id)
                        startActivity(i)
                    },
                    onDelete = { user ->
                        konfirmasiHapus(user)
                    }
                )
            },
            { Toast.makeText(this, "Gagal load user", Toast.LENGTH_SHORT).show() }
        )

        Volley.newRequestQueue(this).add(req)
    }

    private fun konfirmasiHapus(user: UserModel) {
        AlertDialog.Builder(this)
            .setTitle("Hapus User")
            .setMessage("Hapus user ${user.username}?")
            .setPositiveButton("HAPUS") { _, _ ->
                hapusUser(user.id)
            }
            .setNegativeButton("BATAL", null)
            .show()
    }

    private fun hapusUser(id: Int) {
        val url = "http://10.0.2.2:3000/users/$id"

        val req = StringRequest(
            Request.Method.DELETE, url,
            {
                Toast.makeText(this, "User dihapus", Toast.LENGTH_SHORT).show()
                loadData()
            },
            { Toast.makeText(this, "Gagal hapus user", Toast.LENGTH_SHORT).show() }
        )

        Volley.newRequestQueue(this).add(req)
    }
}


