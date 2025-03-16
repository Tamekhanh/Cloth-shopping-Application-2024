package com.example.clothing_store.Bill

data class BillItem(    var address: String? = "",
                        var billId: String = "", // Unique identifier for the item
                        var total: String? = "",
                        var key: Int = 1
   )
