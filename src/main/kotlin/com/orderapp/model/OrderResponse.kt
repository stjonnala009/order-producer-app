package com.orderapp.model

class OrderResponse {

    var items: List<String> = ArrayList<String>()
    var totalPrice: String? = null

    var totalDiscount: String? = "$0.00"
    var totalPriceAfterDiscount: String? = null

    var message: String? = null

}