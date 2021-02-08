package com.orderapp.controller

import com.orderapp.constants.Constants
import com.orderapp.domain.Basket
import com.orderapp.domain.CartItem
import com.orderapp.domain.Price
import com.orderapp.domain.Product
import com.orderapp.kafka.KafkaOrderConfiguration
import com.orderapp.model.OrderRequest
import com.orderapp.model.OrderResponse
import order.exceptions.OutOfStockException
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.text.NumberFormat
import java.util.*


@RestController
class OrderController(val kafkaTemplate: KafkaTemplate<String, String>) {


    @GetMapping("/place-order/offer")
    fun placeOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
        var items = orderRequest.items

        var orderResponse = OrderResponse()

        if (items!!.isEmpty()) {
            orderResponse.message = "Please pass order items"
            return ResponseEntity.ok(orderResponse)
        }
        try {
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "OrderServiceAppWithOffer Started.")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order input ${items}")
            var basket = Basket()

            for (lineItem in items) {
                when (lineItem) {
                    "Apple" -> {
                        basket.addCartItem(CartItem(Product(Constants.APPLE, Price(Constants.APPLE_COST)), 1))
                    }
                    "Orange" -> {
                        basket.addCartItem(CartItem(Product(Constants.ORANGE, Price(Constants.ORANGE_COST)), 1))
                    }
                }
            }

            basket.refreshTotalDiscount()
            val totalPriceBeforeDiscount = NumberFormat.getCurrencyInstance().format(basket.totalPriceBeforeDiscount)

            val totalPriceAfterDiscount = NumberFormat.getCurrencyInstance().format(basket.totalPriceAfterDiscount)
            val totalDiscount = NumberFormat.getCurrencyInstance().format(basket.totalDiscount)

            orderResponse.items = items
            orderResponse.totalPrice = totalPriceBeforeDiscount
            orderResponse.totalDiscount = totalDiscount
            orderResponse.totalPriceAfterDiscount = totalPriceAfterDiscount
            orderResponse.message = "Order Successfully Placed"

            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Successfully Placed")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "totalPrice::${totalPriceBeforeDiscount}")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "totalDiscount::${totalPriceAfterDiscount}")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "totalPriceAfterDiscount::${totalDiscount}")

        } catch (e: OutOfStockException) {

            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Failed: Out of stock")
            orderResponse.message = "Order Failed: Out of stock"
        } catch (e: Exception) {
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Failed: Internal error")
            orderResponse.message = "Order Failed: Internal error"
        } finally {

            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "OrderServiceAppWithOffer Completed.")
        }

        return ResponseEntity.ok(orderResponse)
    }

    @GetMapping("/place-order")
    fun placeOrderNoOffer(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
        var items = orderRequest.items

        var orderResponse = OrderResponse()

        if (items!!.isEmpty()) {

            orderResponse.message = "Please pass order items"

            return ResponseEntity.ok(orderResponse)
        }

        try {
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "OrderServiceAppWithOutOffer Started.")
            var basket = Basket()
            basket.offerMap = Collections.emptyMap()
            for (lineItem in items) {
                when (lineItem) {
                    "Apple" -> {
                        basket.addCartItem(CartItem(Product(Constants.APPLE, Price(Constants.APPLE_COST)), 1))
                    }
                    "Orange" -> {
                        basket.addCartItem(CartItem(Product(Constants.ORANGE, Price(Constants.ORANGE_COST)), 1))
                    }
                }
            }
            basket.refreshTotalDiscount()

            orderResponse.items = items

            val totalPriceBeforeDiscount = NumberFormat.getCurrencyInstance().format(basket.totalPriceBeforeDiscount)

            val totalPriceAfterDiscount = NumberFormat.getCurrencyInstance().format(basket.totalPriceAfterDiscount)
            val totalDiscount = NumberFormat.getCurrencyInstance().format(basket.totalDiscount)

            orderResponse.totalPrice = totalPriceBeforeDiscount

            orderResponse.totalPriceAfterDiscount = totalPriceBeforeDiscount
            orderResponse.message = "Order Successfully Placed"

            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Successfully Placed")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "totalPrice::${totalPriceBeforeDiscount}")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "totalDiscount::${totalPriceAfterDiscount}")
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "totalPriceAfterDiscount::${totalDiscount}")


        } catch (e: OutOfStockException) {

            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Failed: Out of stock")
            orderResponse.message = "Order Failed: Out of stock"
        } catch (e: Exception) {
            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Failed: Internal error")
            orderResponse.message = "Order Failed: Internal error"
        } finally {

            kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "OrderServiceAppWithOutOffer Completed.")
        }

        kafkaTemplate.send(KafkaOrderConfiguration.TOPIC, "Order Placed")

        return ResponseEntity.ok(orderResponse)
    }


}
