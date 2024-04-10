package com.example.myapplication

data class Event (
    val date: String ?= null,
    val time: String ?= null,
    val place: String ?= null,
    val price: String ?= null,
    val description: String ?= null,
    val idBand: String ?= null,
    val emailBand: String ?= null,
    val titulo: String ?= null,
    val imagen: String ?= null
){
    constructor() : this(null, null, null, null, null, null, null, null, null)
}