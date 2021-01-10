package com.babestudios.reachproducts.data.network

import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.ProductsResponse
import com.github.michaelbull.result.Result

interface ReachProductsRepositoryContract {
	suspend fun getProducts(): Result<ProductsResponse, Throwable>
	fun saveCart(cartEntries: List<CartEntry>)
	fun getCartContent(): List<CartEntry>
	fun emptyCart()
}
