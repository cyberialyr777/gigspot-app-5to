package com.example.myapplication

class EventModelo(
    val date: String,
    val time: String,
    val place: String,
    val price: String,
    val description: String,
    val idBand: String,
    val emailBand: String,
    val titulo: String,
    val imagen: String
){
    constructor() : this("", "", "", "", "", "", "", "", "")
}


