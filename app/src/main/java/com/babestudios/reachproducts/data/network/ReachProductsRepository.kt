package com.babestudios.reachproducts.data.network

import com.babestudios.base.network.OfflineException
import com.babestudios.reachproducts.data.local.DatabaseContract
import com.babestudios.reachproducts.data.network.dto.ProductDto
import com.babestudios.reachproducts.data.network.dto.mapAppearance
import com.babestudios.reachproducts.data.network.dto.mapProductDto
import com.babestudios.reachproducts.data.network.dto.mapOccupation
import com.babestudios.reachproducts.model.Product
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import javax.inject.Singleton

@Singleton
class ReachProductsRepository(
	private val reachProductsService: ReachProductsService,
	private val database: DatabaseContract
) : ReachProductsRepositoryContract {
	override suspend fun getProducts(): Result<List<Product>, Throwable> {
		return runCatching {
			productDtoMapper(reachProductsService.getProducts()).also {
				database.saveProductsResponse(it)
			}
		}.mapError { OfflineException(database.getProductsResponse()) }
	}
}

fun productDtoMapper(list: List<ProductDto>): List<Product> =
	list.map { productDto ->
		mapProductDto(
			productDto,
			::mapOccupation,
			::mapAppearance,
		)
	}

