package com.babestudios.reachproducts.ui

import androidx.lifecycle.SavedStateHandle
import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.data.network.dto.ProductsResponseDto
import com.babestudios.reachproducts.data.network.dto.productsResponseDtoMapper
import com.babestudios.reachproducts.data.res.StringResourceHelperContract
import com.babestudios.reachproducts.ext.loadJson
import com.babestudios.reachproducts.model.ProductsResponse
import com.babestudios.reachproducts.navigation.ReachProductsNavigationContract
import com.github.michaelbull.result.Ok
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReachProductsViewModelTest : BaseViewModelTest() {

	private val reachProductsRepositoryContract = mockk<ReachProductsRepositoryContract>(relaxed = true)

	private val reachProductsNavigation = mockk<ReachProductsNavigationContract>()

	private val stringResourceHelper = mockk<StringResourceHelperContract>()

	private var mappedResponse :ProductsResponse= ProductsResponse()

	private var viewModel: ReachProductsViewModel? = null

	@Before
	fun setUp() {
		val response = "products_response".loadJson()
		val responseDto = Gson().fromJson(response, ProductsResponseDto::class.java)
		mappedResponse = productsResponseDtoMapper.invoke(responseDto)
		every {
			stringResourceHelper.getFreeDiscountText(any())
		} answers {
			""
		}
		every {
			stringResourceHelper.getQuantityDiscountText(any(), any())
		} answers {
			""
		}
		every {
			reachProductsNavigation.mainToProductDetails()
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
		(viewModel?.productsLiveData?.value as Ok).value.products.size shouldBe 3
	}

	private fun reachProductsViewModel(): ReachProductsViewModel {
		return ReachProductsViewModel(
			reachProductsRepositoryContract,
			reachProductsNavigation,
			stringResourceHelper,
			SavedStateHandle()
		)
	}
}


