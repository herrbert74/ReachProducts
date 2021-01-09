package com.babestudios.reachproducts.ui.products

import android.view.View
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.ItemProductsBinding
import com.babestudios.reachproducts.model.Product
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem

class ProductsItem(val product: Product) : BindableItem<ItemProductsBinding>() {

    override fun getLayout() = R.layout.item_products

    override fun bind(viewBinding: ItemProductsBinding, position: Int) {
        Glide.with(viewBinding.root).load(product.img).into(viewBinding.ivProductItem)
        viewBinding.lblProductItemName.text = product.name
    }

    override fun initializeViewBinding(view: View) = ItemProductsBinding.bind(view)
}
