package com.example.truck_mixer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // ===== AMBIL DATA DARI LOGIN =====
        val username = intent.getStringExtra("username")
        val role = intent.getStringExtra("role")?.trim()?.lowercase() ?: ""
        val noTm = intent.getStringExtra("no_tm")

        // ===== HEADER USER =====
        val textrole=findViewById<TextView>(R.id.txtRole)
        textrole.text =
            if (!noTm.isNullOrEmpty()) noTm else username ?: "-"
            if (noTm.isNullOrEmpty()){
            textrole.isVisible=false
}
        findViewById<TextView>(R.id.txtRole).text =
            "Role: ${role.replaceFirstChar { it.uppercase() }}"

        // ===== CARD =====
        val cardTmMasuk = findViewById<CardView>(R.id.cardTmMasuk)
        val cardAntrian = findViewById<CardView>(R.id.cardAntrian)
        val cardUpdate = findViewById<CardView>(R.id.cardUpdate)
        val cardLogout = findViewById<CardView>(R.id.cardLogout)

        // ===== CLICK TM MASUK =====
        cardTmMasuk.setOnClickListener {
            val i = Intent(this, TmMasukActivity::class.java)
            i.putExtra("username", username)
            i.putExtra("role", role)
            i.putExtra("no_tm", noTm)
            startActivity(i)
        }

        // ===== CLICK ANTRIAN =====
        cardAntrian.setOnClickListener {
            val i = Intent(this, AntrianDualActivity::class.java)
            i.putExtra("username", username)
            i.putExtra("role", role)
            i.putExtra("no_tm", noTm)
            startActivity(i)
        }

        // ===== ROLE RULE =====
        when (role) {
            "driver" -> {
                cardTmMasuk.visibility = View.VISIBLE
                cardAntrian.visibility = View.VISIBLE
                cardUpdate.visibility = View.GONE
            }
            "super" -> {
                cardTmMasuk.visibility = View.VISIBLE
                cardAntrian.visibility = View.VISIBLE
                cardUpdate.visibility = View.VISIBLE
            }
            else -> {
                cardUpdate.visibility = View.GONE
            }
        }

        // ===== CLICK UPDATE (SUPER ONLY) =====
        cardUpdate.setOnClickListener {
            if (role != "super") {
                Toast.makeText(this, "Akses ditolak", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val i = Intent(this, UpdateActivity::class.java)
            i.putExtra("role", role)
            startActivity(i)
        }

        // ===== LOGOUT =====
        cardLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

