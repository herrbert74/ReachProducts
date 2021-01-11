package com.babestudios.reachproducts.ui.cart

import android.util.Log
import android.view.View
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.ItemCartBinding
import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.Discount
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem

class CartItem(private val cartEntry: CartEntry) : BindableItem<ItemCartBinding>() {

    override fun getLayout() = R.layout.item_cart

    override fun bind(viewBinding: ItemCartBinding, position: Int) {
        Glide.with(viewBinding.root).load(cartEntry.product.image).into(viewBinding.ivCartItem)
        viewBinding.lblCartItemName.text = cartEntry.product.name
        viewBinding.lblCartItemQuantity.text = cartEntry.quantity.toString()
        if (cartEntry.product.discount is Discount.NoDiscount) {
            Log.d("logCatText", "bind hide: ${cartEntry.product.id}")
            viewBinding.lblCartItemDiscount.visibility = View.GONE
        } else {
            Log.d("logCatText", "bind show: ${cartEntry.product.id}")
            viewBinding.lblCartItemDiscount.visibility = View.VISIBLE
            viewBinding.lblCartItemDiscount.text = cartEntry.product.discountText
        }
    }

    override fun initializeViewBinding(view: View) = ItemCartBinding.bind(view)
}
