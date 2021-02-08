package com.orderapp.domain


import com.orderapp.constants.Constants.Companion.APPLE
import com.orderapp.constants.Constants.Companion.APPLE_MAX_LIMIT
import com.orderapp.constants.Constants.Companion.ORANGE
import com.orderapp.constants.Constants.Companion.ORANGE_MAX_LIMIT
import order.exceptions.OutOfStockException
import java.math.BigDecimal
import kotlin.jvm.Throws

class CartItem @JvmOverloads constructor(var product: Product?, var quantity: Int = 1) {


    fun getQuantity(): Int? {

        return quantity
    }

    /**
     * Adds one item to the cart.
     * @param
     * @return quantity.
     */
    @Throws(OutOfStockException::class)
    fun addOne(): Int {

        if (product?.productCode.equals(APPLE) && (quantity + 1 < APPLE_MAX_LIMIT))
            return quantity++
        else if (product?.productCode.equals(ORANGE) && (quantity + 1 < ORANGE_MAX_LIMIT))
            return quantity++
        else {
            println("Out of Stock")
            throw OutOfStockException("Out of Stock")
        }


    }

    fun reduceOne(): Int {
        return quantity - 1.also { quantity = it }
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (product?.hashCode() ?: 0)
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as CartItem
        if (product == null) {
            if (other.product != null) return false
        } else if (product != other.product) return false
        return true
    }

    fun getLineItemTotalBeforeDiscount(): BigDecimal? {
        return product!!.price!!.unitPrice!!.multiply(BigDecimal(quantity))
    }


}