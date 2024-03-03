package com.example.roomongit.selectionmanager.livesource

import androidx.lifecycle.LiveData

class ListItemsLiveData<T>(private val itemsComputer: () -> ArrayList<T>) : LiveData<ArrayList<T>>() {
    override fun onActive() {
        postItems()
    }
    internal fun notifyChange() {
        if(hasActiveObservers()) {
            postItems()
        }
    }
    private fun postItems() {
        postValue(itemsComputer())
    }
}