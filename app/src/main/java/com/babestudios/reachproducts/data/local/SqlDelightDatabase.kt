package com.babestudios.reachproducts.data.local

import com.babestudios.reachproducts.ProductsQueries
import com.babestudios.reachproducts.Database
import com.babestudios.reachproducts.data.network.dto.Status
import com.babestudios.reachproducts.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SqlDelightDatabase @Inject constructor(private val database: Database) : DatabaseContract {
	override fun saveProductsResponse(products: List<Product>) {
		val productsQueries: ProductsQueries = database.productsQueries
		productsQueries.deleteAll()
		products.forEach {
			productsQueries.insertProduct(
				it.id,
				it.name,
				it.occupation,
				it.img,
				it.status.value,
				it.nickname,
				it.appearance
			)
		}
	}

	override fun getProductsResponse(): List<Product> {
		val productsQueries: ProductsQueries = database.productsQueries
		return productsQueries
			.selectAll { id, name, occupation, img, status, nickname, appearance ->
				Product(
					id, name, occupation, img, Status.fromValue(status), nickname, appearance
				)
			}.executeAsList()
	}
}