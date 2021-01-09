package com.babestudios.reachproducts.ui.productdetails

import android.view.View
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.ItemProductDetailsBinding
import com.babestudios.reachproducts.model.Product
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem

class ProductDetailsItem(val product: Product) : BindableItem<ItemProductDetailsBinding>() {

    override fun getLayout() = R.layout.item_product_details

    override fun bind(viewBinding: ItemProductDetailsBinding, position: Int) {
        Glide.with(viewBinding.root).load(product.img).into(viewBinding.ivProductDetailsItem)
        viewBinding.lblProductDetailsItemName.text = product.name
    }

    override fun initializeViewBinding(view: View) = ItemProductDetailsBinding.bind(view)
}
