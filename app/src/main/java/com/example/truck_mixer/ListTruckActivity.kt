package com.example.truck_mixer

import TruckAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class ListTruckActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private val list = ArrayList<TruckModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_truck)

        rv = findViewById(R.id.rvTruck)
        rv.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        fab.setOnClickListener {
            startActivity(Intent(this, AddTruckActivity::class.java))
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val url = "http://10.0.2.2:3000/truck"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                list.clear()
                val arr = JSONArray(response)

                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(
                        TruckModel(
                            o.optString("no_tm", "-"),
                            o.optString("karoseri", "-"),
                            o.optString("nama_driver1", "-"),
                            o.optString("nama_driver2", "-")
                        )
                    )
                }

                rv.adapter = TruckAdapter(
                    list,
                    onEdit = { noTm ->
                        val i = Intent(this, EditTruckActivity::class.java)
                        i.putExtra("no_tm", noTm)
                        startActivity(i)
                    },
                    onDelete = { noTm ->
                        konfirmasiHapus(noTm)
                    }
                )
            },
            {
                Toast.makeText(this, "Gagal load data", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    /* ================= DELETE ================= */

    private fun konfirmasiHapus(noTm: String) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Truck")
            .setMessage("Yakin ingin menghapus $noTm ?")
            .setPositiveButton("HAPUS") { _, _ ->
                hapusTruck(noTm)
            }
            .setNegativeButton("BATAL", null)
            .show()
    }

    private fun hapusTruck(noTm: String) {
        val url = "http://10.0.2.2:3000/truck/$noTm"

        val req = StringRequest(
            Request.Method.DELETE, url,
            {
                Toast.makeText(this, "Truck dihapus", Toast.LENGTH_SHORT).show()
                loadData()
            },
            { error ->
                Toast.makeText(this, error.message ?: "Error hapus", Toast.LENGTH_LONG).show()
                Log.e("DELETE_TRUCK", error.toString())
            }
        )

        Volley.newRequestQueue(this).add(req)
    }

}

