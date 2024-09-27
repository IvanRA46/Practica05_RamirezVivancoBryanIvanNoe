package com.example.codigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val lector = findViewById<Button>(R.id.btnLector)
        val camara = findViewById<Button>(R.id.btnFoto)

        lector.setOnClickListener{lector()}
        camara.setOnClickListener{camara()}

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun lector(){
        val lectorActivity = Intent(applicationContext, Lector::class.java)
        startActivity(lectorActivity)
    }

    fun camara(){
        val fotoActivity = Intent(applicationContext, Foto::class.java)
        startActivity(fotoActivity)
    }
}