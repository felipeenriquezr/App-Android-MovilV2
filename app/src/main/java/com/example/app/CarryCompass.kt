package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.example.app.adapters.ProductAdapter
import com.example.app.models.Product
import com.example.app.network.ApiClient
import com.example.app.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarryCompass : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var productList: MutableList<Product>
    private lateinit var btnIrAlMapa: Button
    private lateinit var agregarProductoButton: Button

    // Suponiendo que tienes un método para obtener el token
    private val token: String = getToken() // Método ficticio para obtener el token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro_compras)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnIrAlMapa = findViewById(R.id.btnIrAlMapa)
        agregarProductoButton = findViewById(R.id.agregarProductoButton)

        productList = mutableListOf()
        // Asegúrate de pasar el token al crear el adaptador
        adapter = ProductAdapter(productList, this, token)
        recyclerView.adapter = adapter

        // Navegar al mapa
        btnIrAlMapa.setOnClickListener {
            irAlMapa()
        }

        // Navegar a la pantalla de agregar producto
        agregarProductoButton.setOnClickListener {
            val intent = Intent(this, CrearActivity::class.java)
            startActivity(intent)
        }

        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    productList.clear()
                    response.body()?.let {
                        productList.addAll(it)
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@CarryCompass,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@CarryCompass, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun irAlMapa() {
        val intent = Intent(this, GeolocationActivity::class.java)
        startActivity(intent)
    }

    // Método ficticio para obtener el token, debes reemplazarlo con tu propia implementación
    private fun getToken(): String {
        return "someTokenValue" // Aquí deberías obtener el token desde tus preferencias o autenticación
    }
}
