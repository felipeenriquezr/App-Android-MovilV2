package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import com.example.app.adapters.ProductAdapter
import com.example.app.models.Product

class CarryCompass : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var productList: MutableList<Product>
    private lateinit var btnIrAlMapa: Button
    private lateinit var agregarProductoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro_compras)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productList = mutableListOf(
            Product("Producto A", "Descripcion 1", "10.99", "https://www.elevencomunicacion.com/wp-content/uploads/2021/02/foto-profesional-producto-restaurante.jpg"),
            Product("Producto B", "Descripcion 2", "15.49", "default.jpg"),
            Product("Producto C", "Descripcion 3", "8.75", "default.jpg"),
            Product("Producto D", "Descripcion 4", "12.30", "default.jpg")
        )

        // Pasar el contexto al adaptador
        adapter = ProductAdapter(productList, this)
        recyclerView.adapter = adapter

        btnIrAlMapa = findViewById(R.id.btnIrAlMapa)
        btnIrAlMapa.setOnClickListener {
            val intent = Intent(this, GeolocationActivity::class.java)
            startActivity(intent)
        }

        // Inicializar y configurar el botón "Agregar Producto"
        agregarProductoButton = findViewById(R.id.agregarProductoButton)
        agregarProductoButton.setOnClickListener {
            val intent = Intent(this, CrearActivity::class.java)
            startActivity(intent)
        }
    }
}
