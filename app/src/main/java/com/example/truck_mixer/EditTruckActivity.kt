package com.example.truck_mixer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class EditTruckActivity : AppCompatActivity() {

    private lateinit var etNoTm: EditText
    private lateinit var etKaroseri: EditText
    private lateinit var etDriver1: EditText
    private lateinit var etDriver2: EditText
    private lateinit var noTm: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_truck)

        etNoTm = findViewById(R.id.etNoTm)
        etKaroseri = findViewById(R.id.etKaroseri)
        etDriver1 = findViewById(R.id.etDriver1)
        etDriver2 = findViewById(R.id.etDriver2)

        val btnUpdate = findViewById<Button>(R.id.btnUpdateTruck)
        noTm = intent.getStringExtra("no_tm") ?: ""

        if (noTm.isEmpty()) {
            Toast.makeText(this, "No TM tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etNoTm.setText(noTm)
        etNoTm.isEnabled = false

        loadDetail(noTm)

        btnUpdate.setOnClickListener {
            updateTruck()
        }
    }

    /* ================= LOAD DETAIL ================= */
    private fun loadDetail(noTm: String) {
        val url = "http://10.0.2.2:3000/truck/$noTm"

        val req = StringRequest(Request.Method.GET, url,
            { res ->
                val obj = JSONObject(res)
                etKaroseri.setText(obj.optString("karoseri", ""))
                etDriver1.setText(obj.optString("nama_driver1", ""))
                etDriver2.setText(obj.optString("nama_driver2", ""))
            },
            {
                Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(req)
    }

    /* ================= UPDATE ================= */
    private fun updateTruck() {
        val karoseri = etKaroseri.text.toString().trim()
        val d1 = etDriver1.text.toString().trim()
        val d2 = etDriver2.text.toString().trim()

        if (karoseri.isEmpty()) {
            Toast.makeText(this, "Karoseri wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://10.0.2.2:3000/truck/$noTm"

        val req = object : StringRequest(
            Request.Method.PUT, url,
            {
                Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            },
            {
                Toast.makeText(this, "Gagal update data", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "karoseri" to karoseri,
                    "nama_driver1" to d1,
                    "nama_driver2" to d2
                )
            }
        }

        Volley.newRequestQueue(this).add(req)
    }


}
