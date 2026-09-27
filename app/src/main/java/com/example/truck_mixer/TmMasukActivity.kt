package com.example.truck_mixer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class TmMasukActivity : AppCompatActivity() {

    private var selectedTm: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tm_masuk)

        val role = intent.getStringExtra("role")?.lowercase() ?: ""
        val noTmLogin = intent.getStringExtra("no_tm")

        // ===== VIEW =====
        val spinnerTm = findViewById<Spinner>(R.id.spinnerTm)
        val txtLabelTm = findViewById<TextView>(R.id.txtLabelTm)
        val cardMyTm = findViewById<CardView>(R.id.cardMyTm)
        val txtMyTm = findViewById<TextView>(R.id.txtMyTm)
        val cardLoading = findViewById<CardView>(R.id.cardLoading)
        val cardRepair = findViewById<CardView>(R.id.cardRepair)

        // DEBUG (WAJIB 1 KALI)
        Log.d("TM_MASUK", "role=$role no_tm=$noTmLogin")

        when (role) {
            "driver" -> {
                // ===== DRIVER LIHAT TM SENDIRI =====
                if (noTmLogin.isNullOrEmpty()) {
                    Toast.makeText(this, "No TM tidak ditemukan!", Toast.LENGTH_LONG).show()
                    finish()
                    return
                }

                cardMyTm.visibility = View.VISIBLE
                txtMyTm.text = noTmLogin   // ⬅️ INI YANG SERING TERLEWAT
                spinnerTm.visibility = View.GONE
                txtLabelTm.visibility = View.GONE

                selectedTm = noTmLogin
            }

            "super" -> {
                // ===== SUPER PILIH TM =====
                txtLabelTm.visibility = View.VISIBLE
                spinnerTm.visibility = View.VISIBLE
                cardMyTm.visibility = View.GONE

                loadAllTm(spinnerTm)
            }
        }

        // ===== KIRIM STATUS =====
        cardLoading.setOnClickListener {
            kirimStatus(selectedTm, "loading")
        }

        cardRepair.setOnClickListener {
            kirimStatus(selectedTm, "perbaikan")
        }
    }

    // ================= LOAD TM UNTUK SUPER =================
    private fun loadAllTm(spinner: Spinner) {
        val url = "http://10.0.2.2:3000/truck"

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val arr = JSONArray(response)
                val listTm = ArrayList<String>()

                for (i in 0 until arr.length()) {
                    listTm.add(arr.getJSONObject(i).getString("no_tm"))
                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    listTm
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedTm = listTm[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
            },
            {
                Toast.makeText(this, "Gagal memuat TM", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(request)
    }

    // ================= KIRIM STATUS =================
    private fun kirimStatus(noTm: String?, status: String) {
        if (noTm.isNullOrEmpty()) {
            Toast.makeText(this, "TM belum ditentukan!", Toast.LENGTH_SHORT).show()
            return
        }

        val endpoint =
            if (status == "loading") "antri_loading" else "perbaikan"
        val url = "http://10.0.2.2:3000/$endpoint"

        val request = object : StringRequest(
            Method.POST, url,
            {
                Toast.makeText(this, "TM $noTm masuk $status", Toast.LENGTH_SHORT).show()
                finish()
            },
            {
                Toast.makeText(this, "TM sudah masuk antrian", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("no_tm" to noTm)
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}

