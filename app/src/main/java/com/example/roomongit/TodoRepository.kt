package com.example.roomongit

import android.util.Log
import com.example.roomongit.dbnew.TodoFB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class TodoRepository(private val database: DatabaseReference)  {
    //private val executor = Executors.newSingleThreadExecutor()
    fun add(title:String, note:String, date:String):Boolean {
        var ret = true
        val userId = database.push().key
        if (userId == null) {
            ret = false
        } else {
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

    fun getAll(): MutableList<TodoFB> {
        val todoList = mutableListOf<TodoFB>()
        //val todoList = mutableListOf<LiveData<TodoFB>>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val taskKey: String = it.key!!
                        if (taskKey != "") {
                            val newItem = it.getValue(TodoFB::class.java)
                            if (newItem != null && taskKey == newItem.id) {
                                Log.d(
                                    "MYRES1",
                                    "${newItem.id}/${newItem.title}/${newItem.note}/${newItem.date}"
                                )
                                todoList.add(newItem)
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return todoList
    }
}


