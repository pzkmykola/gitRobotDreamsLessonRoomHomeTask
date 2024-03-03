package com.example.roomongit.selectionmanager

interface InterceptableSelectionManager : SelectionManager {
    fun addSelectionInterceptor(interceptor: (position: Int, isSelected: Boolean, callback: () -> Unit) -> Unit): Disposable
}