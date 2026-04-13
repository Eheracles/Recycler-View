package com.example.recycler

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductPreferencesManager(context: Context) {

    companion object {
        private const val PREF_NAME = "ProductCatalog"
        private const val KEY_PRODUCTS = "products_list"
    }

    // SharedPreferences guarda los datos en:
    // /data/data/com.example.recycler/shared_prefs/ProductCatalog.xml
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    /** Guarda la lista completa en el XML de SharedPreferences */
    fun saveProducts(products: List<Product>) {
        val json = gson.toJson(products)
        sharedPreferences.edit().putString(KEY_PRODUCTS, json).apply()
    }

    /** Carga la lista desde el XML de SharedPreferences */
    fun loadProducts(): MutableList<Product> {
        val json = sharedPreferences.getString(KEY_PRODUCTS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    /** Agrega un producto y persiste */
    fun addProduct(product: Product) {
        val products = loadProducts()
        products.add(product)
        saveProducts(products)
    }

    /** Elimina un producto por posición y persiste */
    fun removeProduct(position: Int) {
        val products = loadProducts()
        if (position in products.indices) {
            products.removeAt(position)
            saveProducts(products)
        }
    }
}