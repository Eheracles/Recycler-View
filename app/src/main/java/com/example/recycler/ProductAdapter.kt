package com.example.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ProductAdapter(
    private var productList: MutableList<Product>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    /**
     * ViewHolder: guarda las referencias a las vistas de cada tarjeta.
     * Se crea pocas veces (solo las visibles en pantalla) y se reutiliza.
     */
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView     = itemView.findViewById(R.id.tv_product_name)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_product_quantity)
        val tvPrice: TextView    = itemView.findViewById(R.id.tv_product_price)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    /** Crea la vista (inflate del XML) — se llama pocas veces */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    /** Rellena la vista con datos del producto — se llama al reutilizar */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvName.text     = product.name
        holder.tvQuantity.text = "Cantidad: ${product.quantity}"
        holder.tvPrice.text    = String.format(Locale.getDefault(), "Precio: S/ %.2f", product.price)

        holder.btnDelete.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_ID.toInt()) {
                onDelete(pos)
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    /** Actualiza la lista y refresca el RecyclerView */
    fun updateList(newList: MutableList<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}