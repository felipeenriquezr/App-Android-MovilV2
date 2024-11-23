package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.app.models.ProductCreateRequest
import com.example.app.network.ApiClient
import com.example.app.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrearActivity : ComponentActivity() {

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

        // Listener para crear producto
        createButton.setOnClickListener {
            crearProducto()
        }
    }

    private fun crearProducto() {
        val nombre = nameEditText.text.toString().trim()
        val descripcion = descriptionEditText.text.toString().trim()
        val precioTexto = priceEditText.text.toString().trim()

        // Validación de campos
        if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioTexto.toDoubleOrNull()
        if (precio == null) {
            Toast.makeText(this, "Ingrese un precio válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto de solicitud
        val newProduct = ProductCreateRequest(
            name = nombre,
            description = descripcion,
            image = "", // No incluye URL de imagen
            price = precio
        )

        // Consumir el API POST para crear el producto
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val response = apiService.createProduct(newProduct)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CrearActivity,
                            "Producto creado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Regresar a la lista de productos
                        finish()
                    } else {
                        Toast.makeText(
                            this@CrearActivity,
                            "Error al crear producto: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CrearActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
