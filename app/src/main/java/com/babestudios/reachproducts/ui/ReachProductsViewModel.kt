package com.babestudios.reachproducts.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.data.res.StringResourceHelperContract
import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.Discount
import com.babestudios.reachproducts.model.Product
import com.babestudios.reachproducts.model.ProductsResponse
import com.babestudios.reachproducts.navigation.ReachProductsNavigationContract
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import kotlinx.coroutines.launch


class ReachProductsViewModel @ViewModelInject constructor(
    private val reachProductsRepository: ReachProductsRepositoryContract,
    private val reachProductsNavigation: ReachProductsNavigationContract,
    private val stringResourceHelper: StringResourceHelperContract,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var productsLiveData = MutableLiveData<Result<ProductsResponse, Throwable>>()
    var cartLiveData = MutableLiveData<List<CartEntry>>()
    var totalAmountLiveData = MutableLiveData<String>()

    fun bindNavController(navController: NavController) {
        reachProductsNavigation.bind(navController)
    }

    fun popBackStack() {
        reachProductsNavigation.popBackStack()
    }

    fun loadProducts() {
        viewModelScope.launch {
            reachProductsRepository.getProducts().also {
                productsLiveData.value = it.map { response -> addDiscountStrings(response) }
            }
        }
    }

    private fun addDiscountStrings(result: ProductsResponse): ProductsResponse {
        return ProductsResponse(result.products.map {
            it.copy(
                discountText = when (it.discount) {
                    is Discount.GetFreeDiscount -> {
                        stringResourceHelper
                            .getFreeDiscountText(it.discount.quantityPerFreeDiscount)
                    }
                    is Discount.QuantityDiscount -> {
                        stringResourceHelper
                            .getQuantityDiscountText(
                                it.discount.minimumQuantity,
                                it.discount.discountPrice.toDouble()
                            )
                    }
                    else -> null
                }
            )
        })
    }

    fun navigateToProductDetails() {
        reachProductsNavigation.mainToProductDetails()
        reachProductsNavigation.mainToProductDetails()
    }

    fun addToCart(product: Product) {
        val cartEntries = getCartEntries()
        var found = false
        val newCartEntries = cartEntries.map {
            if (it.product == product) {
                found = true
                val q = it.quantity
                CartEntry(product, q + 1)
            } else {
                it
            }
        }.toMutableList()
        if (!found) {
            newCartEntries.add(CartEntry(product, 1))
        }
        reachProductsRepository.saveCart(newCartEntries)
    }

    fun loadCart() {
        val cartEntries = getCartEntries()
        cartLiveData.postValue(cartEntries)
        val totalAmount =
            cartEntries.sumOf { entry -> entry.product.discount.calculateTotalPrice(entry.quantity) }
        totalAmountLiveData.postValue(stringResourceHelper.getTotalAmountString(totalAmount.toDouble()))
    }

    private fun getCartEntries(): List<CartEntry> {
        return reachProductsRepository.getCartContent()
    }

    fun payOrDeleteCart() {
        reachProductsRepository.emptyCart()
        loadCart()
    }

/*fun getProductById(id: Long) {
    if (fullProductList.isEmpty()) {
        selectedProductId = id
        loadProducts()
    } else {
        postProductById(id)
    }
}

private fun postProductById(id: Long) {
    val product = fullProductList.first { product -> product.id == id }
    productLiveData.postValue(product)
}*/

//endregion

//region state


//endregion

}
