package com.babestudios.reachproducts.di

import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.data.network.dto.ProductsResponseDto
import com.babestudios.reachproducts.data.network.dto.productsResponseDtoMapper
import com.babestudios.reachproducts.ext.loadJson
import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.Product
import com.github.michaelbull.result.Ok
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class FakeNetworkModule {

    @Provides
    @Singleton
    fun provideReachProductsRepository(): ReachProductsRepositoryContract {
        val mockkReachProductsRepository =  mockk<ReachProductsRepositoryContract>()
        val response = "products_response".loadJson()
        val responseDto = Gson().fromJson(response, ProductsResponseDto::class.java)
        val mappedResponse = productsResponseDtoMapper.invoke(responseDto)
        coEvery {
            mockkReachProductsRepository.getProducts()
        } returns Ok(mappedResponse)
        val cartEntriesJson = "cart_entries".loadJson()
        val itemType = object : TypeToken<List<CartEntry>>() {}.type
        val cartEntries = Gson().fromJson<List<CartEntry>>(cartEntriesJson, itemType)
        coEvery {
            mockkReachProductsRepository.getCartContent()
        } returns cartEntries

        coEvery {
            mockkReachProductsRepository.saveCart(any())
        } returns Unit
        return mockkReachProductsRepository
    }
}