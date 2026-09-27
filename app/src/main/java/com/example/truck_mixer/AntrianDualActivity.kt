package com.example.truck_mixer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class AntrianDualActivity : AppCompatActivity() {

    private lateinit var rvLoading: RecyclerView
    private lateinit var rvPerbaikan: RecyclerView

    private val listLoading = ArrayList<AntrianModel>()
    private val listPerbaikan = ArrayList<AntrianModel>()

    private lateinit var role: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_antrian_dual)

        // ===== AMBIL ROLE =====
        role = intent.getStringExtra("role")?.trim()?.lowercase() ?: ""
        Log.d("ROLE_DEBUG", "ROLE DARI LOGIN = '$role'")


        // ===== INIT VIEW =====
        rvLoading = findViewById(R.id.rvLoading)
        rvPerbaikan = findViewById(R.id.rvPerbaikan)

        rvLoading.layoutManager = LinearLayoutManager(this)
        rvPerbaikan.layoutManager = LinearLayoutManager(this)

        // ===== LOAD DATA =====
        loadLoading()
        loadPerbaikan()
    }

    /* ================= LOAD LOADING ================= */
    private fun loadLoading() {
        val url = "http://10.0.2.2:3000/list_loading"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                listLoading.clear()
                val arr = JSONArray(response)

                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    listLoading.add(
                        AntrianModel(
                            obj.getInt("id"),
                            obj.getString("no_tm"),
                            obj.getString("status"),
                            obj.getString("waktu")
                        )
                    )
                }

                rvLoading.adapter = AntrianAdapter(listLoading, role) { id, status ->
                    hapusData(id, status)
                }
            },
            { error ->
                Toast.makeText(this, "Gagal memuat loading", Toast.LENGTH_SHORT).show()
                Log.e("LOAD_LOADING", error.message.toString())
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    /* ================= LOAD PERBAIKAN ================= */
    private fun loadPerbaikan() {
        val url = "http://10.0.2.2:3000/list_perbaikan"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                listPerbaikan.clear()
                val arr = JSONArray(response)

                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    listPerbaikan.add(
                        AntrianModel(
                            obj.getInt("id"),
                            obj.getString("no_tm"),
                            obj.getString("status"),
                            obj.getString("waktu")
                        )
                    )
                }

                rvPerbaikan.adapter = AntrianAdapter(listPerbaikan, role) { id, status ->
                    hapusData(id, status)
                }
            },
            { error ->
                Toast.makeText(this, "Gagal memuat perbaikan", Toast.LENGTH_SHORT).show()
                Log.e("LOAD_PERBAIKAN", error.message.toString())
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    /* ================= HAPUS DATA ================= */
    private fun hapusData(id: Int, status: String) {
        val endpoint = when (status.lowercase()) {
            "loading" -> "hapus_loading"
            "perbaikan" -> "hapus_perbaikan"
            else -> return
        }

        val url = "http://10.0.2.2:3000/$endpoint/$id"

        val request = StringRequest(
            Request.Method.DELETE, url,
            {
                Toast.makeText(this, "Berhasil diselesaikan", Toast.LENGTH_SHORT).show()
                loadLoading()
                loadPerbaikan()
            },
            { error ->
                Toast.makeText(this, "Gagal hapus data", Toast.LENGTH_SHORT).show()
                Log.e("HAPUS", error.message.toString())
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}
