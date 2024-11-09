package com.example.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.EditActivity
import com.example.app.R
import com.example.app.models.Product

class ProductAdapter(
    private val productList: MutableList<Product>,
    private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productPrice.text = "$${product.price}"

        Glide.with(context)
            .load(product.imageUrl)
            .into(holder.productImage)

        // Configurar el botón "Editar"
        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java).apply {
                putExtra("nombre", product.name)
                putExtra("descripcion", product.description)
                putExtra("precio", product.price.toDouble())
            }
            context.startActivity(intent)
        }

        // Configurar el botón "Eliminar"
        holder.deleteButton.setOnClickListener {
            productList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.item_nombre)
        val productDescription: TextView = itemView.findViewById(R.id.item_descripcion)
        val productPrice: TextView = itemView.findViewById(R.id.item_precio)
        val productImage: ImageView = itemView.findViewById(R.id.item_image)
        val editButton: Button = itemView.findViewById(R.id.Editar)
        val deleteButton: Button = itemView.findViewById(R.id.Eliminar)
    }
}
