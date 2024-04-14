package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Modelos.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class EventRepository {
    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("eventos")

    @Volatile private var INSTANCE : EventRepository ?= null

    fun getInstance() : EventRepository{
        return INSTANCE ?: synchronized(this){
            val instance = EventRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadEvents(eventsList : MutableLiveData<List<Event>>){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _eventsList : List<Event> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Event::class.java)!!
                    }

                    eventsList.postValue(_eventsList)
                }catch (e : Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}