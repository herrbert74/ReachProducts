package com.babestudios.reachproducts.data.network

import com.babestudios.reachproducts.data.network.dto.ProductsResponseDto
import retrofit2.http.GET

interface ReachProductsService {
	@GET("android/products.json")
	suspend fun getProducts(): ProductsResponseDto
}