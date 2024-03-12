package com.example.roomongit

import android.util.Log
import com.example.roomongit.dbnew.PlaceFB
import com.google.firebase.database.DatabaseReference


class PlaceRepository(private val database: DatabaseReference)  {
    fun add(title:String?, note:String?, date:String):Boolean {
        var ret = true
        val userId = database.push().key
        if (userId == null) {
            ret = false
        }else {
            val todoNew = PlaceFB(id = userId, title = title ?: "", location = note ?: "", urlImage = date)
            database.child(todoNew.id).setValue(todoNew).addOnCompleteListener {
                if(!it.isSuccessful) ret = false
            }
        }
        return ret
    }

    fun remove (todo: PlaceFB) {
        val userId = todo.id
        database.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                task.result.children.forEach {
                    val newItem = it.key
                    if ( newItem != null && userId == newItem) {
                        database.child(todo.id).removeValue().addOnSuccessListener {
                            Log.d(
                                "MYRES2",
                                "Field with id = $userId removed!!!"
                            )
                            return@addOnSuccessListener
                        }
                    }
                }
            }
        }
    }
}


