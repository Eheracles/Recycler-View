package com.example.recycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var prefsManager: ProductPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager = ProductPreferencesManager(this)

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crea el Adapter con la lista cargada desde SharedPreferences (XML)
        adapter = ProductAdapter(prefsManager.loadProducts()) { position ->
            // Callback que se llama al pulsar el botón eliminar
            prefsManager.removeProduct(position)
            adapter.updateList(prefsManager.loadProducts())
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        // Botón flotante para agregar productos
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            showAddProductDialog()
        }
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)

        val etName     = dialogView.findViewById<TextInputEditText>(R.id.et_name)
        val etQuantity = dialogView.findViewById<TextInputEditText>(R.id.et_quantity)
        val etPrice    = dialogView.findViewById<TextInputEditText>(R.id.et_price)

        AlertDialog.Builder(this)
            .setTitle("Agregar Producto")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val name     = etName.text.toString().trim()
                val qtyStr   = etQuantity.text.toString().trim()
                val priceStr = etPrice.text.toString().trim()

                if (name.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val product = Product(
                    name     = name,
                    quantity = qtyStr.toInt(),
                    price    = priceStr.toDouble()
                )

                // Guarda en SharedPreferences → escribe en ProductCatalog.xml
                prefsManager.addProduct(product)
                adapter.updateList(prefsManager.loadProducts())
                Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}