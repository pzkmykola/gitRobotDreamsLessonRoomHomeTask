package com.example.roomongit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeListFragment : Fragment() {
    private lateinit var  listView: RecyclerView
    private lateinit var adapter: PlaceListAdapter
    private lateinit var viewModel: PlaceViewModel
    private val target = MyApplication.getApp().target

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_list_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: FloatingActionButton = view.findViewById(R.id.fabButton)
        val fabRunMap = view.findViewById<FloatingActionButton>(R.id.fabMap)
        viewModel = ViewModelProvider(this@HomeListFragment)[PlaceViewModel::class.java]
        listView = view.findViewById(R.id.list)
        listView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlaceListAdapter()
        listView.adapter = adapter

        fab.setOnClickListener {
            val activity = requireActivity() as OnAddClickListener
            activity.onFabClick()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddPlaceFragment())
                .commit()
        }

        target.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val placeList = mutableListOf<PlaceFB>()
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val taskKey: String = it.key!!
                        if (taskKey != "") {
                            val newItem = it.getValue(PlaceFB::class.java)
                            if (newItem != null && taskKey == newItem.id) {
                                Log.d(
                                    "MYRES1",
                                    "${newItem.id}/${newItem.title}/${newItem.location}/${newItem.urlImage}"
                                )
                                placeList.add(newItem)
                            }
                        }
                    }
                    adapter = PlaceListAdapter(placeList)
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
                    viewModel.remove(adapter.items[viewHolder.adapterPosition])
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(listView)


        fabRunMap.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)
            parentFragmentManager.beginTransaction()
                .add(com.google.android.material.R.id.container, SupportMapFragment())
                .addToBackStack("mapFragment")
                .commit()
        }
    }
}