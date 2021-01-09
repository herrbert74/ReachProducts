package com.babestudios.reachproducts.navigation

import androidx.navigation.NavController
import com.babestudios.base.navigation.BaseNavigation
import com.babestudios.base.ext.navigateSafe
import com.babestudios.reachproducts.ui.products.ProductsFragmentDirections

class ReachProductsNavigation : BaseNavigation(), ReachProductsNavigationContract {

	override var navController: NavController? = null

	override fun mainToProductDetails(id: Long) {
		val action =
			ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(id)
		navController?.navigateSafe(action)
	}
}
