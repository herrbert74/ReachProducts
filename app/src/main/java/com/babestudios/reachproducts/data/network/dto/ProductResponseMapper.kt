package com.babestudios.reachproducts.data.network.dto

import com.babestudios.reachproducts.model.Discount
import com.babestudios.reachproducts.model.Product
import java.math.BigDecimal
import java.math.RoundingMode

fun mapProductDto(
	input: ProductDto,
): Product {
	val price = BigDecimal(input.price).setScale(2, RoundingMode.HALF_EVEN)
	return Product(
		input.id,
		input.name,
		input.image,
		price,
		mapProductToDiscount(input.id, price)
	)
}

fun mapProductToDiscount(id: String, price: BigDecimal): Discount {
	return when(id) {
		"LIPSTICK" -> Discount.GetFreeDiscount(2, price)
		"EYELINER" -> Discount.QuantityDiscount(
			3,
			BigDecimal("30.00"),
			price
		)
		else -> Discount.NoDiscount(price)
	}
}
