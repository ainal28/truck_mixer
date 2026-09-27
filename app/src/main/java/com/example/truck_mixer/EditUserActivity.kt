package com.example.truck_mixer

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class EditUserActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNoTm: EditText
    private lateinit var spinnerRole: Spinner

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        userId = intent.getIntExtra("id", 0)
        if (userId == 0) {
            Toast.makeText(this, "User tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etNoTm = findViewById(R.id.etNoTm)
        spinnerRole = findViewById(R.id.spinnerRole)

        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val roles = arrayOf("super", "driver", "batcher")
        spinnerRole.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                etNoTm.visibility =
                    if (roles[position] == "driver") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadDetail()

        btnUpdate.setOnClickListener { updateUser() }
        btnDelete.setOnClickListener { konfirmasiHapus() }
    }

    /* ================= LOAD DETAIL ================= */
    private fun loadDetail() {
        val url = "http://10.0.2.2:3000/users"

        val req = StringRequest(Request.Method.GET, url,
            { res ->
                val arr = org.json.JSONArray(res)
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    if (o.getInt("id") == userId) {
                        etUsername.setText(o.getString("username"))
                        val role = o.getString("role")
                        spinnerRole.setSelection(
                            (spinnerRole.adapter as ArrayAdapter<String>).getPosition(role)
                        )
                        etNoTm.setText(o.optString("no_tm", ""))
                        break
                    }
                }
            },
            {
                Toast.makeText(this, "Gagal memuat data user", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(req)
    }

    /* ================= UPDATE ================= */
    private fun updateUser() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val role = spinnerRole.selectedItem.toString()
        val noTm = etNoTm.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "Username wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (role == "driver" && noTm.isEmpty()) {
            Toast.makeText(this, "Driver wajib punya No TM", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://10.0.2.2:3000/users/$userId"

        val req = object : StringRequest(
            Request.Method.PUT, url,
            {
                Toast.makeText(this, "User berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            },
            {
                Toast.makeText(this, "Gagal update user", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["role"] = role
                params["no_tm"] = if (role == "driver") noTm else ""
                if (password.isNotEmpty()) params["password"] = password
                return params
            }
        }

        Volley.newRequestQueue(this).add(req)
    }

    /* ================= DELETE ================= */
    private fun konfirmasiHapus() {
        AlertDialog.Builder(this)
            .setTitle("Hapus User")
            .setMessage("Yakin ingin menghapus user ini?")
            .setPositiveButton("HAPUS") { _, _ -> hapusUser() }
            .setNegativeButton("BATAL", null)
            .show()
    }

    private fun hapusUser() {
        val url = "http://10.0.2.2:3000/users/$userId"

        val req = StringRequest(Request.Method.DELETE, url,
            {
                Toast.makeText(this, "User berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            },
            {
                Toast.makeText(this, "Gagal hapus user", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(req)
    }
}
