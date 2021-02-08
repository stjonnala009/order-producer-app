package com.orderapp.domain

class Product(var productCode: String, var price: Price) {


    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + 10
        return result
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Product

        if (this.productCode != other.productCode) return false

        return true
    }

    override fun toString(): String {

        return "Offer [productCode=$productCode, price=$price]"
    }


}