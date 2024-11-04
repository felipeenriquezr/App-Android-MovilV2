package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent

class CrearActivity: ComponentActivity() {


        private lateinit var nameEditText: EditText
        private lateinit var descriptionEditText: EditText
        private lateinit var priceEditText: EditText
        private lateinit var createButton: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.crearprd)

            // Inicializar vistas
            nameEditText = findViewById(R.id.nameEditText)
            descriptionEditText = findViewById(R.id.descriptionEditText)
            priceEditText = findViewById(R.id.prieceEditText)
            createButton = findViewById(R.id.createButton)

            // Configurar listener para el botón
            createButton.setOnClickListener {
                crearProducto()
            }
        }

        private fun crearProducto() {
            val nombre = nameEditText.text.toString().trim()
            val descripcion = descriptionEditText.text.toString().trim()
            val precioTexto = priceEditText.text.toString().trim()

            // Validación simple de los campos
            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            // Convertir precio a número
            val precio = precioTexto.toDoubleOrNull()
            if (precio == null) {
                Toast.makeText(this, "Ingrese un precio válido", Toast.LENGTH_SHORT).show()
                return
            }

            // Crear el producto (esto puede ser donde guardes el producto en una base de datos o envíes a un servidor)
            Toast.makeText(this, "Producto creado exitosamente: $nombre", Toast.LENGTH_SHORT).show()

            // Limpiar campos después de crear el producto
            nameEditText.text.clear()
            descriptionEditText.text.clear()
            priceEditText.text.clear()
        }
    }
