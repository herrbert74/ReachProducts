package com.babestudios.reachproducts.data.local

import com.babestudios.reachproducts.CartQueries
import com.babestudios.reachproducts.Database
import com.babestudios.reachproducts.ProductsQueries
import com.babestudios.reachproducts.data.network.dto.mapProductToDiscount
import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.Product
import com.babestudios.reachproducts.model.ProductsResponse
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SqlDelightDatabase @Inject constructor(private val database: Database) : DatabaseContract {
	override fun saveProductsResponse(productsResponse: ProductsResponse) {
		val productsQueries: ProductsQueries = database.productsQueries
		productsQueries.deleteAll()
		productsResponse.products.forEach {
			productsQueries.insertProduct(
				it.id,
				it.name,
				it.image,
				it.price.toString(),
			)
		}
	}

	override fun getProductsResponse(): ProductsResponse {
		return ProductsResponse(database.productsQueries
			.selectAll { id, name, image, price ->
				val priceBigDecimal = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN)
				Product(
					id,
					name,
					image,
					priceBigDecimal,
					mapProductToDiscount(id, priceBigDecimal)
				)
			}.executeAsList())
	}

	override fun saveCart(cartEntries: List<CartEntry>) {
		val cartQueries: CartQueries = database.cartQueries
		cartQueries.deleteAll()
		cartEntries.forEach {
			cartQueries.insertProduct(
				it.product.id,
				it.product.name,
				it.product.image,
				it.product.price.toString(),
				it.quantity.toLong()
			)
		}
	}

	override fun getCartContent(): List<CartEntry> {
		return database.cartQueries
			.selectAll { id, name, image, price, quantity ->
				val priceBigDecimal = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN)
				CartEntry(
					Product(
						id,
						name,
						image,
						priceBigDecimal,
						mapProductToDiscount(id, priceBigDecimal)
					), quantity.toInt()
				)
			}.executeAsList()
	}

	override fun emptyCart() {
		database.cartQueries.deleteAll()
	}
}