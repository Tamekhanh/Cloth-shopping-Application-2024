package com.example.clothing_store.account

data class UserData(
    var nameLast: String = "",
    var nameFirst: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var phone: String="",
    //val address: String= ""// Nếu bạn cần thêm một field
)
