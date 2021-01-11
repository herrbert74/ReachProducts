package com.babestudios.reachproducts.mappers

import com.babestudios.reachproducts.data.network.dto.ProductsResponseDto
import com.babestudios.reachproducts.data.network.dto.productsResponseDtoMapper
import com.babestudios.reachproducts.ext.loadJson
import com.babestudios.reachproducts.model.ProductsResponse
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MappersTest {

	private var mappedResponse: ProductsResponse = ProductsResponse()

	@BeforeEach
	fun setup() {
		val response = "products_response".loadJson()
		val responseDto = Gson().fromJson(response, ProductsResponseDto::class.java)
		mappedResponse = productsResponseDtoMapper.invoke(responseDto)
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse.products[0].name shouldBe "Express Lipstick"
	}
}
