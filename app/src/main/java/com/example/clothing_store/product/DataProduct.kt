// DataClass.kt
package com.example.clothing_store.product

class DataProduct {
    var dataname: String? = null
        private set
    var datasize: String? = null
        private set
    var dataprice: Int? = null
        private set
    var dataamount: Int? = null
        private set
    var dataImage: String? = null
        private set
    var key: String? = null
    var username: String? = null

    constructor(dataname: String?, datasize: String?, dataprice: Int?, dataamount: Int?, dataImage: String?, key: String?) {
        this.dataname = dataname
        this.datasize = datasize
        this.dataprice = dataprice
        this.dataamount = dataamount
        this.dataImage = dataImage
        this.key = key
    }

    constructor()
}

