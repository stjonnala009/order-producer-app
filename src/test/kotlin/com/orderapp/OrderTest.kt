package com.orderapp

import com.orderapp.domain.*

import org.junit.Assert

import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.text.NumberFormat

import org.junit.jupiter.api.Test
import java.util.*

@SpringBootTest
class OrderTest {
    private var basket = Basket()

    val price_5: Price = Price(BigDecimal(0.60))
    val price_10: Price = Price(BigDecimal(0.25))
    val orange: Product = Product("Orange", price_10)
    val apple: Product = Product("Apple", price_5)

    val Item_1 = CartItem(apple, 1)
    val Item_2 = CartItem(apple, 1)
    val Item_4 = CartItem(orange, 1)
    val Item_5 = CartItem(orange, 1)
    val Item_6 = CartItem(orange, 1)
    val Item_3 = CartItem(apple, 1)


    @Test
    fun testAddMultipleQuantityofProductWithRemovingOneUnit() {
        basket = Basket()

        basket.addCartItem(Item_1)
        basket.addCartItem(Item_2)
        basket.addCartItem(Item_3)
        basket.addCartItem(Item_4)

        Assert.assertTrue(2 == basket.totalLineItem)
        Assert.assertTrue(3 == basket.cartItems.get(0).getQuantity())

        basket.decreaseByOne(Item_4)
        Assert.assertTrue(1 == basket.totalLineItem)
        Assert.assertTrue(3 == basket.cartItems.get(0).getQuantity())
    }

    @Test
    fun testAddApplesVerifyTotalWithoutOffer() {
        basket = Basket()
        basket.offerMap = Collections.emptyMap()
        //3 apples
        basket.addCartItem(Item_1)
        basket.addCartItem(Item_2)
        basket.addCartItem(Item_3)

        Assert.assertTrue(1 == basket.totalLineItem)
        basket.refreshTotalDiscount()

        val totalPrice = NumberFormat.getCurrencyInstance().format(basket.totalPriceBeforeDiscount)

        val totalPriceAfterDiscount = NumberFormat.getCurrencyInstance().format(basket.totalPriceAfterDiscount)

        Assert.assertTrue("$1.80" == totalPrice)
        Assert.assertTrue("$1.80" == totalPriceAfterDiscount)
    }

    @Test
    fun testAddOrangesVerifyTotalWithoutOffer() {
        basket = Basket()
        //3 oranges
        basket.addCartItem(Item_4)
        basket.addCartItem(Item_4)
        basket.addCartItem(Item_4)

        Assert.assertTrue(1 == basket.totalLineItem)

        basket.refreshTotalDiscount()
        val totalPrice = NumberFormat.getCurrencyInstance().format(basket.totalPriceBeforeDiscount)

        val totalPriceAfterDiscount = NumberFormat.getCurrencyInstance().format(basket.totalPriceAfterDiscount)

        Assert.assertTrue("$0.75" == totalPrice)
        Assert.assertTrue("$0.75" == totalPriceAfterDiscount)

    }


}