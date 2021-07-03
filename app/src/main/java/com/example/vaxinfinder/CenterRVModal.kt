package com.example.vaxinfinder

data class CenterRVModal (
    val centerName: String,
    val centerAddr: String,
    val fromTime: String,
    val toTime: String,
    val vaccine: String,
    val price : String,
    val agelimit: Int,
    val availability: Int
    )