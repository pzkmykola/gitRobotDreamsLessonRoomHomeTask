package com.example.roomongit

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.roomongit.databinding.InputFragmentLayoutBinding
import com.example.roomongit.dbnew.PlaceFB
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar

class AddPlaceFragment(private var place: PlaceFB? = null): BottomSheetDialogFragment() {
    private lateinit var binding:InputFragmentLayoutBinding
    private var closeFunction: ((Boolean) -> Unit)? = null
    private var addTodoSuccess: Boolean = false
    private lateinit var viewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this@AddPlaceFragment)[PlaceViewModel::class.java]
        binding = InputFragmentLayoutBinding.bind(view)
        place?.let {
            binding.placeTitle.setText(it.title)
            binding.placeLocation.setText(it.location)
        }

        binding.addButton.setOnClickListener {
            val title = binding.placeTitle.text?.toString()
            val location = binding.placeLocation.text?.toString()

            if((title == "" || title == null) ||
                (location == "" || location == null)) {
                snackbar("Some fields are empty, please try again")
            }
            else {
                if(viewModel.add(title, location, urlImage = "")){
                    snackbar("Task updated successfully")
                    closeFunction?.invoke(true)
                    this.dismiss()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeListFragment())
                        .commit()
                } else{
                    snackbar("Something is wrong, try again!!!")
                }
            }
//            val target = database.reference.child(
//                account?.id ?: "unknown_account"
//            )
//                .child("clients")
//                .child(UUID.randomUUID().toString())
//            target.setValue(
//                inputField.text.toString()
//            ).addOnCompleteListener {
//                parentFragmentManager.popBackStack()
//            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(true) //(addPlaceSuccess)
    }

    private fun snackbar(msg: String){
        Snackbar.make(requireActivity().findViewById(R.id.frameLayoutAddPlace),msg, Snackbar.LENGTH_LONG).show()
    }
}