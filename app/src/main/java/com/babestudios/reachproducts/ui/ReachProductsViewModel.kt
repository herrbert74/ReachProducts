package com.babestudios.reachproducts.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.model.Product
import com.babestudios.reachproducts.navigation.ReachProductsNavigationContract
import com.babestudios.reachproducts.ui.products.ProductsFragmentDirections
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import kotlinx.coroutines.launch

private const val QUERY_KEY: String = "queryText"

class ReachProductsViewModel @ViewModelInject constructor(
    private val reachProductsRepository: ReachProductsRepositoryContract,
    private val reachProductsNavigation: ReachProductsNavigationContract,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //state not saved
    private var fullProductList: List<Product> = emptyList()

    //exposed state
    var productsLiveData = MutableLiveData<Result<List<Product>, Throwable>>()
    var productLiveData = MutableLiveData<Product?>()

    //transient state (for process death)
    private var selectedProductId: Long? = null

    fun bindNavController(navController: NavController) {
        reachProductsNavigation.bind(navController)
    }

    fun popBackStack() {
        reachProductsNavigation.popBackStack()
    }

    fun loadProducts() {
        viewModelScope.launch {
            reachProductsRepository.getProducts().also {
                productsLiveData.value = it
                fullProductList = it.get() ?: emptyList()
                selectedProductId?.let { id ->
                    selectedProductId = null
                    getProductById(id)
                }
            }
        }
    }

    fun navigateToProductDetails(id: Long) {
        ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment()
        reachProductsNavigation.mainToProductDetails(id)
    }

    fun getProductById(id: Long) {
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
    }

    //endregion

    //region state

    fun getQuery() = savedStateHandle.get<String>(QUERY_KEY) ?: ""

    private fun setQueryText(query: String) = savedStateHandle.set(QUERY_KEY, query).also {
        applyFilters()
    }

    //endregion

}
