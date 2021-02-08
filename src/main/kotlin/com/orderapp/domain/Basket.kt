package com.orderapp.domain


import com.orderapp.constants.Constants
import order.exceptions.OutOfStockException
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer
import kotlin.jvm.Throws

class Basket {

    var offerMap: MutableMap<String?, Offer?> = HashMap()

    private val cartItemList: MutableList<CartItem> = ArrayList()

    var totalPriceBeforeDiscount: BigDecimal = BigDecimal(0)


    var totalPriceAfterDiscount: BigDecimal = BigDecimal(0)


    var totalDiscount: BigDecimal = BigDecimal(0)


    private val discount = Discount()

    init {

        val appleProduct = Product(Constants.APPLE, Price(BigDecimal(0.60)))

        val appleOffer = Offer(appleProduct, 2, DiscountType.TWO_FOR_ONE.discount)

        offerMap.put(Constants.APPLE, appleOffer)

        //initialize orange and add offer
        val orangeProduct = Product(Constants.ORANGE, Price(BigDecimal(0.25)))
        val orangeOffer = Offer(orangeProduct, 3, DiscountType.THREE_FOR_TWO.discount)
        offerMap.put(Constants.ORANGE, orangeOffer)
    }

    /**
     * Adds cartItem to the cart.
     * @param [cartItem] to this cart
     * @return void.
     */
    @Throws(OutOfStockException::class)
    fun addCartItem(cartItem: CartItem) {

        var cartItem = cartItem

        val idxPos = cartItemList.indexOfFirst {
            it.product?.productCode == cartItem.product?.productCode
        }

        if (idxPos != -1) {
            cartItem = cartItemList[idxPos]
            cartItem.addOne()
        } else {
            if (cartItem?.product?.productCode.equals(Constants.APPLE) && (cartItem.quantity + 1 < Constants.APPLE_MAX_LIMIT)) {
                cartItemList.add(cartItem)
            } else if (cartItem?.product?.productCode.equals(Constants.ORANGE) && (cartItem?.quantity + 1 < Constants.ORANGE_MAX_LIMIT)) {
                cartItemList.add(cartItem)

            } else {
                println("Out of Stock")
                throw OutOfStockException("Out of Stock")
            }
        }
        refreshTotal()

    }

    val totalLineItem: Int
        get() = cartItemList.size

    fun removeCartItem(cartItem: CartItem?): Boolean {
        val isLineItemRemoved = cartItemList.remove(cartItem)
        refreshTotal()
        return isLineItemRemoved
    }

    fun decreaseByOne(cartItem: CartItem): Boolean {
        var cartItem = cartItem
        var isLineItemReduced = false
        val idxPos = cartItemList.indexOf(cartItem)
        if (idxPos != -1) {
            cartItem = cartItemList[idxPos]
            val qty = cartItem.reduceOne()
            if (qty == 0) {
                cartItemList.remove(cartItem)
            }
            isLineItemReduced = true
            refreshTotal()
        }
        return isLineItemReduced
    }

    fun refreshTotalDiscount() {
        totalDiscount = discount.calculateDiscount(offerMap, cartItemList)

        totalPriceAfterDiscount = totalPriceBeforeDiscount!!.subtract(totalDiscount)
    }

    private fun refreshTotal() {
        totalPriceBeforeDiscount = BigDecimal(0)
        cartItemList.forEach(Consumer { item: CartItem ->
            totalPriceBeforeDiscount = totalPriceBeforeDiscount.add(item.getLineItemTotalBeforeDiscount())
        })
    }

    val cartItems: List<CartItem>
        get() = Collections.unmodifiableList(cartItemList)

}