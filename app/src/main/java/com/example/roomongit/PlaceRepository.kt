package com.example.roomongit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class PlaceRepository(val database: DatabaseReference)  {
    fun add(title:String?, location:String?, urlImage:String):Boolean {
        var ret = true
        val placeId = database.push().key
        if (placeId == null) {
            ret = false
        }else {
            val todoNew = PlaceFB(id = placeId, title = title ?: "", location = location ?: "", urlImage = urlImage)
            database.child(todoNew.id).setValue(todoNew).addOnCompleteListener {
                if(!it.isSuccessful) ret = false
            }
        }
        return ret
    }

    fun remove (place: PlaceFB) {
        val userId = place.id
        database.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                task.result.children.forEach {
                    val newItem = it.key
                    if ( newItem != null && userId == newItem) {
                        database.child(place.id).removeValue().addOnSuccessListener {
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
//
//    fun getAll():MutableList<PlaceFB> {
//        val resLiveData by lazy { MutableLiveData<MutableList<PlaceFB>>()}
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val placeList = mutableListOf<PlaceFB>()
//                    snapshot.children.forEach {
//                        val taskKey: String = it.key!!
//                        if (taskKey != "") {
//                            val newItem = it.getValue(PlaceFB::class.java)
//                            if (newItem != null && taskKey == newItem.id) {
//                                Log.d(
//                                    "MYRES1",
//                                    "${newItem.id}/${newItem.title}/${newItem.location}/${newItem.urlImage}"
//                                )
//                                placeList.add(newItem)
//                            }
//                        }
//                    }
//                    //adapter.updateItems(placeList)
//                    //adapter = PlaceListAdapter(placeList)
//                    //listView.adapter = adapter
//                    resLiveData.value?.addAll(placeList)
//                }
//            }
//
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//        return resLiveData
//    }
}


