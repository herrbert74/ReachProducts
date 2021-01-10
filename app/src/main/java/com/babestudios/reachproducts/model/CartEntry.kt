package com.babestudios.reachproducts.model

const val PRIME = 31

data class CartEntry(val product: Product, val quantity: Int) {
    override fun equals(other: Any?): Boolean {
        return product == (other as CartEntry).product
    }

    override fun hashCode(): Int {
        var result = product.hashCode()
        result = PRIME * result + quantity
        return result
    }
}
