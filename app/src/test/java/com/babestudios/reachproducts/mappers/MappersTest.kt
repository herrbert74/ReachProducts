package com.babestudios.reachproducts.mappers

import com.babestudios.reachproducts.data.network.productDtoMapper
import com.babestudios.reachproducts.data.network.dto.ProductDto
import com.babestudios.reachproducts.data.network.dto.Status
import com.babestudios.reachproducts.model.Product
import com.babestudios.base.ext.loadJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class MappersTest {

	private var mappedResponse :List<Product> = emptyList()

	@Before
	fun setup() {
		val response = "products_response".loadJson()
		val gson = Gson()
		val itemType = object : TypeToken<List<ProductDto>>() {}.type
		val responseDto = gson.fromJson<List<ProductDto>>(response, itemType)
		mappedResponse = productDtoMapper(responseDto)
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse[0].name shouldBe "Walter White"
	}

	@Test
	fun `when there is a response then occupation is mapped`() {
		mappedResponse[0].occupation shouldBe "High School Chemistry Teacher, Meth King Pin"
	}

	@Test
	fun `when there is a response then status is mapped`() {
		mappedResponse[0].status shouldBe Status.Unknown
	}

	@Test
	fun `when there is a response then appearance is mapped`() {
		mappedResponse[0].appearance.replace(" ", "") shouldBe "1,2,3,4,5"
	}

}
