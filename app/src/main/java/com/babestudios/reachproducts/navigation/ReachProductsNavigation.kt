package com.babestudios.reachproducts.navigation

import androidx.navigation.NavController
import com.babestudios.base.navigation.BaseNavigation
import com.babestudios.base.ext.navigateSafe
import com.babestudios.reachproducts.R

class ReachProductsNavigation : BaseNavigation(), ReachProductsNavigationContract {

	override var navController: NavController? = null

	override fun mainToProductDetails() {
		navController?.navigateSafe(R.id.action_productsFragment_to_cartFragment)
	}
}
