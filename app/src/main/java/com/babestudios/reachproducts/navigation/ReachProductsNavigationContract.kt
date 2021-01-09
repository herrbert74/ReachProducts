package com.babestudios.reachproducts.navigation

import com.babestudios.base.navigation.BaseNavigationContract

interface ReachProductsNavigationContract : BaseNavigationContract {
	fun mainToProductDetails(id: Long)
}
