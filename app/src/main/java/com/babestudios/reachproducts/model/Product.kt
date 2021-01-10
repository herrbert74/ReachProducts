package com.babestudios.reachproducts.model

import java.math.BigDecimal

data class Product(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val discount: Discount = Discount.NoDiscount(price),
    val discountText: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        return (other as Product).id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
