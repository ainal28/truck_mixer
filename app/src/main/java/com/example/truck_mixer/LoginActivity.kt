package com.example.truck_mixer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Username/Password belum diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val url = "http://10.0.2.2:3000/login"

            val request = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    val json = JSONObject(response)
                    val message = json.optString("message")
                    val username = json.optString("username")
                    val role = json.optString("role")
                    val noTm = json.optString("no_tm")

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    when(role) {
                        "driver", "super" -> {
                            val i = Intent(this, MainMenuActivity::class.java)
                            i.putExtra("username", username)
                            i.putExtra("role", role)

                            i.putExtra("no_tm", noTm)
                            startActivity(i)
                            finish()
                        }
                        "batcher", "operator" -> {
                            val i = Intent(this, AntrianDualActivity::class.java)
                            i.putExtra("role", role)
                            startActivity(i)
                            finish()
                        }
                    }
                },
                { error ->
                    Toast.makeText(this, "Gagal login!", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = user.uppercase()  // biar konsisten
                    params["password"] = pass
                    return params
                }
            }

            Volley.newRequestQueue(this).add(request)
        }
    }
}
