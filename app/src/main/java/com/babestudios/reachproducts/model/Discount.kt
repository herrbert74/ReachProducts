package com.babestudios.reachproducts.model

import java.io.Serializable
import java.math.BigDecimal
import kotlin.math.floor

sealed class Discount(
    open val price: BigDecimal
) : Serializable {

    abstract fun calculateTotalPrice(quantity: Int): BigDecimal

    class NoDiscount(
        override val price: BigDecimal,
    ) : Discount(price) {
        override fun calculateTotalPrice(quantity: Int): BigDecimal {
            return (quantity.toBigDecimal() * price).setScale(2)
        }

    }

    class GetFreeDiscount(
        val quantityPerFreeDiscount: Int,
        override val price: BigDecimal,
    ) : Discount(price) {

        override fun calculateTotalPrice(quantity: Int): BigDecimal {
            val payableAmount = quantity - floor(quantity.toDouble() / quantityPerFreeDiscount)
            return (price * payableAmount.toBigDecimal()).setScale(2)
        }
    }

    class QuantityDiscount(
        val minimumQuantity: Int,
        val discountPrice: BigDecimal,
        override val price: BigDecimal,
    ) : Discount(price) {

        override fun calculateTotalPrice(quantity: Int): BigDecimal {
            return (if (quantity >= minimumQuantity)
                quantity.toBigDecimal() * discountPrice
            else
                quantity.toBigDecimal() * price).setScale(2)
        }
    }


}