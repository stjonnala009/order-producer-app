package com.orderapp.domain


import java.math.BigDecimal

class Discount {
    fun applyDiscount(lineItem: CartItem): BigDecimal {
        var discount = BigDecimal(0)

        val product = lineItem.product
        if (product != null) {
            val productCode = product.productCode

            when (productCode) {


                "Apple" -> {
                    discount = lineItem.getLineItemTotalBeforeDiscount()!!.subtract(
                            lineItem.getLineItemTotalBeforeDiscount()!!.multiply(DiscountType.TWO_FOR_ONE.discount)
                    )

                }

                "Orange" -> {
                    discount = lineItem.getLineItemTotalBeforeDiscount()!!
                            .subtract(
                                    lineItem.getLineItemTotalBeforeDiscount()!!.multiply(DiscountType.THREE_FOR_TWO.discount)
                            )
                }
            }
        }

        return discount
    }

    fun calculateDiscount(
            offerMap: Map<String?, Offer?>,
            set: List<CartItem>
    ): BigDecimal {
        var totalDiscount = BigDecimal(0)
        //apply discount if the offer matches with line item
        if (offerMap.isEmpty()) {
            return totalDiscount
        }
        for (item in set) {

            var offerItemQuantity = offerMap!![item.product?.productCode]?.quantity!!

            var itemQuantity = item.getQuantity()!!

            if (offerItemQuantity <= itemQuantity) {
                //case when cart item quantity is more than offer
                // eg. when cart item are 3 and offer is 2 For 1
                val mod: Int = item.quantity % offerItemQuantity

                val quantityOnWhichOfferIsApplied: Int = if (mod == 0) item.quantity else
                    itemQuantity - mod

                totalDiscount =
                        totalDiscount.add(applyDiscount(CartItem(item.product, quantityOnWhichOfferIsApplied)))
            }
        }
        return totalDiscount
    }
}