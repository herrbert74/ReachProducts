package com.babestudios.reachproducts.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.util.stream.Stream

class DiscountTest {

    @ParameterizedTest
    @MethodSource
    fun `when discount is applied then total price is right`(
        discount: Discount,
        quantity: Int,
        expectedResult: BigDecimal
    ) {
        Assertions.assertEquals(
            expectedResult, discount.calculateTotalPrice(quantity)
        )
    }

    companion object {

        @JvmStatic
        fun `when discount is applied then total price is right`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Discount.QuantityDiscount(
                    3,
                    BigDecimal("2.50"),
                    BigDecimal("3.00")
                ), 2,  BigDecimal("6.00")),
                Arguments.of(Discount.QuantityDiscount(
                    3,
                    BigDecimal("2.50"),
                    BigDecimal("3.00")
                ), 10,  BigDecimal("25.00")),
                Arguments.of(Discount.GetFreeDiscount(
                    2,
                    BigDecimal("3.00")
                ), 2,  BigDecimal("3.00")),
                Arguments.of(Discount.GetFreeDiscount(
                    2,
                     BigDecimal("3.00")
                ), 3,  BigDecimal("6.00")),
                Arguments.of(Discount.GetFreeDiscount(
                    2,
                    BigDecimal("3.00")
                ), 4,  BigDecimal("6.00")),
                Arguments.of(Discount.GetFreeDiscount(
                    3,
                    BigDecimal("3.00")
                ), 3,  BigDecimal("6.00")),
            )
        }
    }
}