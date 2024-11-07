package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.ProductAdapter
import com.example.app.models.Product

/*
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CarryCompass: ComponentActivity() {
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro_compras)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ListaProductos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.activity_carro_compras, container, false)
        var recyclerView = view.findViewById<RecyclerView> (R.id.ListaProductos)
        var productos = ArrayList<Product>()
        productos.add(Product("Item 1","25", "Este es un producto de prueba", "default.jpg"))
        productos.add(Product("Item 2","25", "Este es un producto de prueba", "default.jpg"))
        productos.add(Product("Item 3","25", "Este es un producto de prueba", "default.jpg"))
        productos.add(Product("Item 4","25", "Este es un producto de prueba", "default.jpg"))
        productos.add(Product("Item 5","25", "Este es un producto de prueba", "default.jpg"))
        productos.add(Product("Item 6","25", "Este es un producto de prueba", "default.jpg"))

        var adapter = ProductAdapter(productos)
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        return view
    }

   /* companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CarryCompass().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

}*/

// ProductListActivity.kt



class CarryCompass: ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var productList: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro_compras)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear una lista de productos quemados
        productList = listOf(
            Product("Producto A", "Descripcion 1","10.99", "https://www.elevencomunicacion.com/wp-content/uploads/2021/02/foto-profesional-producto-restaurante.jpg"),
            Product("Producto B", "Descripcion 2","15.49", "default.jpg"),
            Product("Producto C", "Descripcion 3","8.75", "default.jpg"),
            Product("Producto D", "Descripcion 4","12.30", "default.jpg")
        )

        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter
    }
}

