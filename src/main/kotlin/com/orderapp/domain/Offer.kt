package com.orderapp.domain

import java.math.BigDecimal

class Offer(val product: Product, val quantity: Int, val discount: BigDecimal) {

    override fun toString(): String {
        return "Offer [product=$product, quantity=$quantity, discountType=$discount]"
    }

}