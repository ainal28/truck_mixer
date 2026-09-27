package com.example.truck_mixer

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AddUserActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNoTm: EditText
    private lateinit var spinnerRole: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etNoTm = findViewById(R.id.etNoTm)
        spinnerRole = findViewById(R.id.spinnerRole)
        etUsername.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etUsername, InputMethodManager.SHOW_IMPLICIT)


        val btnSave = findViewById<Button>(R.id.btnSave)

        // ===== ROLE SPINNER =====
        val roles = arrayOf("super", "driver", "batcher")
        spinnerRole.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val role = roles[position]
                etNoTm.visibility = if (role == "driver") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSave.setOnClickListener {
            simpanUser()
        }
    }

    private fun simpanUser() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val role = spinnerRole.selectedItem.toString()
        val noTm = etNoTm.text.toString().trim()

        // ===== VALIDASI =====
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username & Password wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (role == "driver" && noTm.isEmpty()) {
            Toast.makeText(this, "Driver wajib punya No TM", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://10.0.2.2:3000/users"

        val req = object : StringRequest(
            Request.Method.POST, url,
            {
                Toast.makeText(this, "User berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            },
            { error ->
                val code = error.networkResponse?.statusCode
                if (code == 409) {
                    Toast.makeText(this, "Username sudah digunakan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal menambah user", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                params["role"] = role
                if (role == "driver") params["no_tm"] = noTm
                return params
            }
        }

        Volley.newRequestQueue(this).add(req)
    }
}
