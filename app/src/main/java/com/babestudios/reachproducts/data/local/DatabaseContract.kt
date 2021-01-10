package com.babestudios.reachproducts.data.local

import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.ProductsResponse

interface DatabaseContract {
    fun saveProductsResponse(productsResponse: @JvmSuppressWildcards ProductsResponse)
    fun getProductsResponse(): ProductsResponse
    fun saveCart(cartEntries: @JvmSuppressWildcards List<CartEntry>)
    fun getCartContent(): List<CartEntry>
    fun emptyCart()
}