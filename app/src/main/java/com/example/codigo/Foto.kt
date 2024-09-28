package com.example.codigo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Foto : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btnTomarFoto: Button
    private lateinit var btnGuardarFoto: Button
    private lateinit var btnLimpiarFoto: Button
    private lateinit var imageView: ImageView
    private var fotoBitmap: Bitmap? = null

    companion object {
        private const val STORAGE_PERMISSION_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnTomarFoto = findViewById(R.id.btnFotot)
        btnGuardarFoto = findViewById(R.id.btnGuardarFoto)
        btnLimpiarFoto = findViewById(R.id.btnLimpiarFoto)
        imageView = findViewById(R.id.imgFoto)

        checkPermissions()

        btnTomarFoto.setOnClickListener { tomarFoto() }
        btnGuardarFoto.setOnClickListener { guardarFoto() }
        btnLimpiarFoto.setOnClickListener { limpiarFoto() }

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun limpiarFoto() {
        // Limpiar el Bitmap y el ImageView
        fotoBitmap = null
        imageView.setImageResource(0) // Establece la imagen en vacío
        Toast.makeText(this, "Foto limpiada", Toast.LENGTH_SHORT).show()
    }

    private fun checkPermissions() {
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
        } else {
            // El permiso ya ha sido concedido
            Toast.makeText(this, "Permiso de almacenamiento ya concedido", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                for (i in permissions.indices) {
                    val permission = permissions[i]
                    val result = grantResults[i]
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permiso concedido para $permission", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Permiso denegado para $permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun tomarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(intent)
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val extras = result.data?.extras
                fotoBitmap = extras?.get("data") as Bitmap
                imageView.setImageBitmap(fotoBitmap)
            } else {
                Toast.makeText(this, "Error al tomar la foto", Toast.LENGTH_SHORT).show()
            }
        }

    private fun guardarFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            fotoBitmap?.let { bitmap ->
                val archivo = File(getExternalFilesDir(null), "foto_${System.currentTimeMillis()}.jpg")
                try {
                    FileOutputStream(archivo).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        Toast.makeText(this, "Foto guardada: ${archivo.absolutePath}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
                }
            } ?: Toast.makeText(this, "No hay foto para guardar", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permiso de almacenamiento no concedido", Toast.LENGTH_SHORT).show()
        }
    }
}

