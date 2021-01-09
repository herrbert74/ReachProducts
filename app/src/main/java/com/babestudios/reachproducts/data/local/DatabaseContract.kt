package com.babestudios.reachproducts.data.local

import com.babestudios.reachproducts.model.Product

interface DatabaseContract {
    fun saveProductsResponse(products: @JvmSuppressWildcards List<Product>)
    fun getProductsResponse(): List<Product>
}