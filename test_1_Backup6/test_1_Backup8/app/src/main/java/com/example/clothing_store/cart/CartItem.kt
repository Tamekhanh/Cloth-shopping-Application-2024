data class CartItem(
    var itemId: String = "", // Unique identifier for the item
    var image: String? = null,
    var size: String? = null,
    var name: String? = null,
    var price: Double? = null,
    var key: String? = null,
    var quantity: Int = 1, // Default quantity
)
