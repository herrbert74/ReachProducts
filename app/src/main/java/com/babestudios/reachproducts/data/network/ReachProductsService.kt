package com.babestudios.reachproducts.data.network

import com.babestudios.reachproducts.data.network.dto.ProductDto
import retrofit2.http.GET

interface ReachProductsService {
	@GET("api/products")
	suspend fun getProducts(): List<ProductDto>
}