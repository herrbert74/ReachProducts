package com.babestudios.reachproducts.data.network

import com.babestudios.base.network.OfflineException
import com.babestudios.reachproducts.data.local.DatabaseContract
import com.babestudios.reachproducts.data.network.dto.productsResponseDtoMapper
import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.model.ProductsResponse
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import javax.inject.Singleton

@Singleton
class ReachProductsRepository(
    private val reachProductsService: ReachProductsService,
    private val database: DatabaseContract
) : ReachProductsRepositoryContract {
    override suspend fun getProducts(): Result<ProductsResponse, Throwable> {
        return runCatching {
            productsResponseDtoMapper(reachProductsService.getProducts()).also {
                database.saveProductsResponse(it)
            }
        }.mapError { OfflineException(database.getProductsResponse()) }
    }

    override fun saveCart(cartEntries: List<CartEntry>) {
        database.saveCart(cartEntries)
    }

    override fun getCartContent(): List<CartEntry> {
        return database.getCartContent()
    }

    override fun emptyCart() {
        database.emptyCart()
    }
}
