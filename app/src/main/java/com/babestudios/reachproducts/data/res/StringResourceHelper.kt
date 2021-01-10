package com.babestudios.reachproducts.data.res

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.reachproducts.R
import javax.inject.Inject
import javax.inject.Singleton


interface StringResourceHelperContract {
    fun getTotalAmountString(totalAmount: Double): String
    fun getFreeDiscountText(quantityPerFreeDiscount: Int): String
    fun getQuantityDiscountText(
        minimumQuantity: Int,
        discountPrice: Double
    ): String
}

@Singleton
class StringResourceHelper @Inject constructor(@ApplicationContext val context: Context) :
    StringResourceHelperContract {

    override fun getTotalAmountString(totalAmount: Double): String {
        return String.format(context.getString(R.string.total_amount), totalAmount)
    }

    override fun getFreeDiscountText(quantityPerFreeDiscount: Int): String {
        return String.format(
            context.resources.getString(R.string.get_free_discount),
            quantityPerFreeDiscount
        )
    }

    override fun getQuantityDiscountText(minimumQuantity: Int, discountPrice: Double): String {
        return String.format(
            context.resources.getString(R.string.quantity_discount),
            minimumQuantity,
            discountPrice
        )
    }
}
