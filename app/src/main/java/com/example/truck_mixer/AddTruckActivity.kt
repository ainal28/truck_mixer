package com.example.truck_mixer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AddTruckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_truck)

        val etNoTm = findViewById<EditText>(R.id.etNoTm)
        val etKaroseri = findViewById<EditText>(R.id.etKaroseri)
        val etDriver1 = findViewById<EditText>(R.id.etDriver1)
        val etDriver2 = findViewById<EditText>(R.id.etDriver2)
        val btnSave = findViewById<Button>(R.id.btnSaveTruck)

        btnSave.setOnClickListener {
            val noTm = etNoTm.text.toString().trim()
            val karoseri = etKaroseri.text.toString().trim()
            val driver1 = etDriver1.text.toString().trim()
            val driver2 = etDriver2.text.toString().trim()

            if (noTm.isEmpty() || karoseri.isEmpty()) {
                Toast.makeText(this, "No TM dan Karoseri wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tambahTruck(noTm, karoseri, driver1, driver2)
        }
    }

    private fun tambahTruck(
        noTm: String,
        karoseri: String,
        driver1: String,
        driver2: String
    ) {
        val url = "http://10.0.2.2:3000/truck"

        val request = object : StringRequest(
            Request.Method.POST, url,
            {
                Toast.makeText(this, "Truck Mixer berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            },
            {
                Toast.makeText(this, "Gagal menambah Truck Mixer", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "no_tm" to noTm,
                    "karoseri" to karoseri,
                    "nama_driver1" to driver1,
                    "nama_driver2" to driver2
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
