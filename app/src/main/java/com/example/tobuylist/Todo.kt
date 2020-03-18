package com.example.tobuylist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Todo(
    @PrimaryKey var id: Long = 0,
    var name : String = "",
    var url: String = "",
    var price : Int = 0,
    var imp : Int = 0
) : RealmObject(){


}