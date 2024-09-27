package com.example.codigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.integration.android.IntentIntegrator

data class Registro(val codigo: String, val descripcion: String, val infoAdicional1: String, val infoAdicional2: String)

class Lector : AppCompatActivity() {

    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var infoAdicional1: EditText
    private lateinit var infoAdicional2: EditText
    private lateinit var btnEscanear: Button
    private lateinit var btnCapturar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnBuscar: Button

    private lateinit var toolbar: Toolbar

    private var registros = Array(10) { Registro("", "", "", "") }
    private var posicionActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lector)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        codigo = findViewById(R.id.edtCodigo)
        descripcion = findViewById(R.id.edtDescripcion)
        infoAdicional1 = findViewById(R.id.edtInfoAdicional1)
        infoAdicional2 = findViewById(R.id.edtInfoAdicional2)
        btnEscanear = findViewById(R.id.btnescanear)
        btnCapturar = findViewById(R.id.btnRegistrar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnBuscar = findViewById(R.id.btnBuscar)

        btnEscanear.setOnClickListener { escanearCodigo() }
        btnCapturar.setOnClickListener { capturarDatos() }
        btnBuscar.setOnClickListener { buscarRegistro() }
        btnLimpiar.setOnClickListener { limpiar() }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun limpiar() {
        codigo.text.clear()
        descripcion.text.clear()
        infoAdicional1.text.clear()
        infoAdicional2.text.clear()
    }

    private fun escanearCodigo() {
        val intentIntegrator = IntentIntegrator(this@Lector)
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Lector de cámara")
        intentIntegrator.setCameraId(0)
        intentIntegrator.setBeepEnabled(true)
        intentIntegrator.setBarcodeImageEnabled(true)
        intentIntegrator.initiateScan()
    }

    private fun capturarDatos() {
        if (codigo.text.toString().isNotEmpty() && descripcion.text.toString().isNotEmpty()) {
            if (posicionActual < registros.size) {
                registros[posicionActual] = Registro(
                    codigo.text.toString(),
                    descripcion.text.toString(),
                    infoAdicional1.text.toString(),
                    infoAdicional2.text.toString()
                )
                posicionActual++
                Toast.makeText(this, "Datos capturados", Toast.LENGTH_SHORT).show()
                limpiar()
            } else {
                Toast.makeText(this, "Alcanzado el límite de registros", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Debe registrar datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarRegistro() {
        val codigoBuscado = codigo.text.toString()
        val registroEncontrado = registros.find { it.codigo == codigoBuscado }

        if (registroEncontrado != null) {
            descripcion.setText(registroEncontrado.descripcion)
            infoAdicional1.setText(registroEncontrado.infoAdicional1)
            infoAdicional2.setText(registroEncontrado.infoAdicional2)
            Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_SHORT).show()
            } else {
                codigo.setText(result.contents)
                Toast.makeText(this, "Código escaneado: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
