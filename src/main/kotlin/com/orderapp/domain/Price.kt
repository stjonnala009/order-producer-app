package com.orderapp.domain

import java.math.BigDecimal
import java.text.NumberFormat

class Price(var unitPrice: BigDecimal) {

    override fun toString(): String {
        return "Price [unitPrice=${NumberFormat.getCurrencyInstance().format(unitPrice)}]"
    }
}