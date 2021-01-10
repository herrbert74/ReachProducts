package com.babestudios.reachproducts.ui.cart

import android.view.View
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.ItemCartBinding
import com.babestudios.reachproducts.model.CartEntry
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem

class CartItem(private val cartEntry: CartEntry) : BindableItem<ItemCartBinding>() {

    override fun getLayout() = R.layout.item_cart

    override fun bind(viewBinding: ItemCartBinding, position: Int) {
        Glide.with(viewBinding.root).load(cartEntry.product.image).into(viewBinding.ivCartItem)
        viewBinding.lblCartItemName.text = cartEntry.product.name
        viewBinding.lblCartItemQuantity.text = cartEntry.quantity.toString()
    }

    override fun initializeViewBinding(view: View) = ItemCartBinding.bind(view)
}
