package com.babestudios.reachproducts.data.network.dto

import com.babestudios.base.data.mapNullInputList
import com.babestudios.reachproducts.model.ProductsResponse

data class ProductsResponseDto(val products: List<ProductDto> = emptyList())

val productsResponseDtoMapper : (ProductsResponseDto) -> ProductsResponse =
    { productsResponseDto ->
        ProductsResponse(mapNullInputList(productsResponseDto.products) { productDto ->
            mapProductDto(
                productDto,
            )
        })
    }
