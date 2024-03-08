package com.example.roomongit

import android.util.Log
import com.example.roomongit.dbnew.TodoFB
import com.google.firebase.database.DatabaseReference



class TodoRepository(private val database: DatabaseReference)  {
    fun add(title:String?, note:String?, date:String):Boolean {
        var ret = true
        val userId = database.push().key
        if (userId == null) {
            ret = false
        }else {
            val todoNew = TodoFB(id = userId, title = title ?: "", note = note ?: "", date = date)
            database.child(todoNew.id).setValue(todoNew).addOnCompleteListener {
                if(!it.isSuccessful) ret = false
            }
        }
        return ret
    }

    fun remove (todo: TodoFB) {
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


