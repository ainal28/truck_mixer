package com.example.truck_mixer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class UpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val role = intent.getStringExtra("role")?.lowercase() ?: ""
        Log.d("UPDATE_DEBUG", "ROLE MASUK UPDATE = $role")
        if (role != "super") {
            Toast.makeText(this, "Akses ditolak", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContentView(R.layout.activity_update)

        findViewById<CardView>(R.id.cardTruck).setOnClickListener {
            val i = Intent(this, ListTruckActivity::class.java)
            i.putExtra("role", "super")   // 🔥 TERUSKAN ROLE
            startActivity(i)
        }

        findViewById<CardView>(R.id.cardUser).setOnClickListener {
            val i = Intent(this, ListUserActivity::class.java)
            i.putExtra("role", "super")
            startActivity(i)
        }

    }
}

