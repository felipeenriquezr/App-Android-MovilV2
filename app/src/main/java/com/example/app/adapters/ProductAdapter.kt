package com.example.app.adapters

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.app.EditActivity
import com.example.app.R
import com.example.app.models.Product
import com.example.app.network.ApiClient
import com.example.app.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter(
    private val productList: MutableList<Product>,
    private val context: Context,
    private val token: String // Añade el token como parámetro
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position] // Usar posición directamente aquí

        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productPrice.text = "$${product.price}"

        Glide.with(context)
            .load(product.image)
            .into(holder.productImage)

        // Configurar el botón "Editar"
        holder.editButton.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val currentProduct = productList[currentPosition]
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra("id", currentProduct.id) // Enviar ID como Int
                    putExtra("nombre", currentProduct.name)
                    putExtra("descripcion", currentProduct.description)
                    putExtra("precio", currentProduct.price)
                }
                context.startActivity(intent)
            }
        }

        // Configurar el botón "Eliminar"
        holder.deleteButton.setOnClickListener {
            val currentPosition = holder.adapterPosition // Usar adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)

                apiService.deleteProduct(product.id!!, token).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show()
                            productList.removeAt(currentPosition) // Usa posición actualizada
                            notifyItemRemoved(currentPosition)
                        } else {
                            Toast.makeText(context, "Error al eliminar el producto: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
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
