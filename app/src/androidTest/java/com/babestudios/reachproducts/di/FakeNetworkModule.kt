package com.babestudios.reachproducts.di

import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.data.network.productDtoMapper
import com.babestudios.reachproducts.data.network.dto.ProductDto
import com.babestudios.base.ext.loadJson
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
        val gson = Gson()
        val itemType = object : TypeToken<List<ProductDto>>() {}.type
        val responseDto = gson.fromJson<List<ProductDto>>(response, itemType)
        val mappedResponse = productDtoMapper(responseDto)
        coEvery {
            mockkReachProductsRepository.getProducts()
        } returns Ok(mappedResponse)
        return mockkReachProductsRepository
    }
}