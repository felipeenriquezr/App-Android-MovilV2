package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class EditActivity : ComponentActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editarprd)

        // Inicializar vistas
        nameEditText = findViewById(R.id.editarprdEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        priceEditText = findViewById(R.id.prieceEditText)
        saveButton = findViewById(R.id.createButton)

        // Cargar los datos del producto a editar (puedes recibir estos datos a través de un Intent)
        cargarDatosProducto()

        // Configurar listener para el botón de guardar
        saveButton.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosProducto() {
        // Aquí cargarías los datos del producto. Ejemplo:
        val nombre = intent.getStringExtra("nombre") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)

        nameEditText.setText(nombre)
        descriptionEditText.setText(descripcion)
        priceEditText.setText(precio.toString())
    }

    private fun guardarCambios() {
        val nombre = nameEditText.text.toString().trim()
        val descripcion = descriptionEditText.text.toString().trim()
        val precioTexto = priceEditText.text.toString().trim()

        // Validación simple de los campos
        if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir precio a número
        val precio = precioTexto.toDoubleOrNull()
        if (precio == null) {
            Toast.makeText(this, "Ingrese un precio válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar cambios (puedes actualizar el producto en una base de datos o enviar los datos a un backend)
        Toast.makeText(this, "Producto actualizado: $nombre", Toast.LENGTH_SHORT).show()

        // Terminar la actividad y regresar
        finish()
    }
}