package com.example.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.models.Product

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        var vista= LayoutInflater.from(parent.context).inflate(R.layout.item_producto,parent,false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.bindItems(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        init {

        }
        fun bindItems (product: Product){
            val nombre= itemView.findViewById<TextView>(R.id.item_nombre)
            nombre.text = product.name
            val descripcion = itemView.findViewById<TextView>(R.id.item_descripcion)
            descripcion.text = product.description
            val precio = itemView.findViewById<TextView>(R.id.item_precio)
            precio.text = product.price
        }
    }

}

/*
// ProductAdapter.kt
import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.models.Product

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.descriptionTextView.text = product.description
        holder.priceTextView.text = "$${product.price}"
        holder.imageTextView.text = product.image
    }

    override fun getItemCount() = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTextView: TextView = itemView.findViewById(R.id.text1)
        val descriptionTextView: TextView = itemView.findViewById(R.id.text2)
        val nameTextView: TextView = itemView.findViewById(R.id.text1)
        val priceTextView: TextView = itemView.findViewById(R.id.text3)
    }
}*/
