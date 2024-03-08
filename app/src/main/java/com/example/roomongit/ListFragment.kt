package com.example.roomongit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.roomongit.dbnew.TodoFB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListFragment : Fragment() {
    private val target = MyApplication.getApp().target
    private lateinit var  listView: RecyclerView
    private lateinit var adapter: TodoListAdapter

    private val repo = MyApplication.getApp().repo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: FloatingActionButton = view.findViewById(R.id.fabButton)
        listView = view.findViewById(R.id.list)
        listView.layoutManager = LinearLayoutManager(requireContext())

        fab.setOnClickListener {
            val activity = requireActivity() as OnAddClickListener
            activity.onFabClick()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddTodoFragment())
                .commit()
        }

        target.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todoList = mutableListOf<TodoFB>()
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
                    adapter = TodoListAdapter(todoList)
                    listView.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int = makeMovementFlags(0, ItemTouchHelper.END)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.END) {
                    repo.remove(adapter.items[viewHolder.adapterPosition])
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(listView)
    }
}