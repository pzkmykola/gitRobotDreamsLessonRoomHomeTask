package com.example.roomongit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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
import com.squareup.picasso.Picasso


class HomeListFragment : Fragment() {
    private lateinit var  listView: RecyclerView
    private lateinit var adapter: PlaceListAdapter
    private lateinit var viewModel: PlaceViewModel
    //private val target = MyApplication.getApp().target
    private lateinit var placeList: MutableList<PlaceFB>
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
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is PlaceViewModel.UIState.Empty -> Unit

                is PlaceViewModel.UIState.Processing -> Unit

                is PlaceViewModel.UIState.Result -> {
                    adapter.updateItems(uiState.placeList)
                    listView.adapter = adapter
                }

                is PlaceViewModel.UIState.InMap -> Unit
                is PlaceViewModel.UIState.ImageMap -> Unit
            }

        }

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

        fab.setOnClickListener {
            val activity = requireActivity() as OnAddClickListener
            activity.onFabClick()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddPlaceFragment())
                .commit()
        }

        fabRunMap.setOnClickListener {
            viewModel.goToMap()
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(com.google.android.material.R.id.container, SupportMapFragment())
                .commit()
        }

//        parentFragmentManager.addOnBackStackChangedListener {
//            parentFragmentManager
//                .fragments
//                .lastOrNull()?.onResume()
//        }
    }

//    override fun onResume() {
//        super.onResume()
//        Toast.makeText(requireContext(), "Return from Maps",Toast.LENGTH_LONG).show()
//    }
}