package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.models.Product
import com.example.app.models.ProductUpdateRequest
import com.example.app.network.ApiClient
import com.example.app.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class EditActivity : ComponentActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var saveButton: Button
    private var productId: Int? = null // Cambiar a Int para facilitar el uso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editarprd)

        // Inicializar vistas
        nameEditText = findViewById(R.id.editarprdEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        priceEditText = findViewById(R.id.prieceEditText)
        saveButton = findViewById(R.id.createButton)

        // Cargar los datos del producto a editar
        cargarDatosProducto()

        // Configurar listener para el botón de guardar
        saveButton.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosProducto() {
        // Obtener el ID del producto del Intent
        productId = intent.getIntExtra("id", -1) // Cambiar a getIntExtra

        if (productId == -1) { // Verificar si el ID no es válido
            Toast.makeText(this, "Error: el ID del producto no es válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Llamar al endpoint para obtener los datos del producto
        lifecycleScope.launch {
            try {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val response: Response<Product> = apiService.getProductById(productId!!)

                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        nameEditText.setText(product.name)
                        descriptionEditText.setText(product.description)
                        priceEditText.setText(product.price.toString())
                    } else {
                        Toast.makeText(this@EditActivity, "Error: no se encontró el producto", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@EditActivity, "Error al cargar el producto: $errorMessage", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
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

        // Crear el objeto de solicitud para la API
        val productUpdateRequest = ProductUpdateRequest(
            name = nombre,
            description = descripcion,
            price = precio
        )

        // Llamar a la API para actualizar el producto
        lifecycleScope.launch {
            try {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val response: Response<Void> = apiService.updateProduct(productId!!, productUpdateRequest)

                if (response.isSuccessful) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this@EditActivity, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@EditActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
