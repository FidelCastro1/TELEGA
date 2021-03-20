package com.example.telega.telega.models

data class User(
        var id: String = "",
        var username: String = "",
        var bio: String = "",
        var fullname: String = "",
        var state: String = "",
        var phone: String = "",
        var photoUrl: String = "empty"
)