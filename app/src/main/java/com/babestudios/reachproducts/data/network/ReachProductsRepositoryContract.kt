package com.babestudios.reachproducts.data.network

import com.babestudios.reachproducts.model.Product
import com.github.michaelbull.result.Result

interface ReachProductsRepositoryContract {
	suspend fun getProducts(): Result<List<Product>, Throwable>
}
