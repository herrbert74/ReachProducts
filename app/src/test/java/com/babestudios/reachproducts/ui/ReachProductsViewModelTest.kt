package com.babestudios.reachproducts.ui

import androidx.lifecycle.SavedStateHandle
import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.data.network.productDtoMapper
import com.babestudios.reachproducts.data.network.dto.ProductDto
import com.babestudios.reachproducts.model.Product
import com.babestudios.reachproducts.navigation.ReachProductsNavigationContract
import com.babestudios.base.ext.loadJson
import com.github.michaelbull.result.Ok
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReachProductsViewModelTest : BaseViewModelTest() {

	private val reachProductsRepositoryContract = mockk<ReachProductsRepositoryContract>(relaxed = true)

	private val reachProductsNavigation = mockk<ReachProductsNavigationContract>()

	private var mappedResponse :List<Product> = emptyList()

	private var viewModel: ReachProductsViewModel? = null

	@Before
	fun setUp() {
		val response = "products_response".loadJson()
		val gson = Gson()
		val itemType = object : TypeToken<List<ProductDto>>() {}.type
		val responseDto = gson.fromJson<List<ProductDto>>(response, itemType)
		mappedResponse = productDtoMapper(responseDto)
		every {
			reachProductsNavigation.mainToProductDetails(any())
		} answers
				{
					Exception("")
				}
		coEvery {
			reachProductsRepositoryContract.getProducts()
		} answers {
			Ok(mappedResponse)
		}
		viewModel = reachProductsViewModel()
	}

	@Test
	fun `when loadProducts is called then liveData is posted`() {
		viewModel?.loadProducts()
		(viewModel?.productsLiveData?.value as Ok).value.size shouldBe 63
	}

	private fun reachProductsViewModel(): ReachProductsViewModel {
		return ReachProductsViewModel(
			reachProductsRepositoryContract,
			reachProductsNavigation,
			SavedStateHandle()
		)
	}
}


