package com.babestudios.reachproducts.ui.products

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.ItemProductBinding
import com.babestudios.reachproducts.model.Discount
import com.babestudios.reachproducts.model.Product
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem

class ProductItem(val product: Product) : BindableItem<ItemProductBinding>() {

    override fun getLayout() = R.layout.item_product

    override fun bind(viewBinding: ItemProductBinding, position: Int) {
        Glide.with(viewBinding.root).load(product.image).into(viewBinding.ivProductItem)
        viewBinding.lblProductItemName.text = product.name
        viewBinding.lblProductItemPrice.text =
            String.format(viewBinding.root.context.getString(R.string.price), product.price)
        if (product.discount is Discount.NoDiscount) {
            viewBinding.lblProductItemDiscount.visibility = GONE
        } else {
            viewBinding.lblProductItemDiscount.visibility = VISIBLE
            viewBinding.lblProductItemDiscount.text = product.discountText
        }
    }

    override fun initializeViewBinding(view: View) = ItemProductBinding.bind(view)
}
