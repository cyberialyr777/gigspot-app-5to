package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventViewModel : ViewModel(){
    private val repository : EventRepository
    private val _allEvents = MutableLiveData<List<Event>>()
    val allEvents : LiveData<List<Event>> = _allEvents

    init {
        repository = EventRepository().getInstance()
        repository.loadEvents(_allEvents)
    }
}