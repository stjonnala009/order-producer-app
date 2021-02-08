package com.orderapp.constants

import java.math.BigDecimal

enum class DiscountType(val discount: BigDecimal) {
    TWO_FOR_ONE(BigDecimal(0.50)),
    THREE_FOR_TWO(BigDecimal(0.33));
}