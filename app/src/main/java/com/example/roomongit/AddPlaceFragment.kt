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

class AddPlaceFragment(private var todo: PlaceFB? = null): BottomSheetDialogFragment() {
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
        todo?.let {
            binding.todoTitle.setText(it.title)
            binding.todoDescription.setText(it.location)
            binding.dateDropdown.setText(it.urlImage)
        }
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
        binding.dateDropdown.setOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")
        }
        datePicker.addOnPositiveButtonClickListener {
            binding.dateDropdown.setText(datePicker.headerText)
        }

        binding.addButton.setOnClickListener {
            val title = binding.todoTitle.text?.toString()
            val note = binding.todoDescription.text?.toString()
            val date = binding.dateDropdown.text?.toString()

            if((title == "" || title == null) ||
                (note == "" || note == null) ||
                (date == "" || date == null)) {
                snackbar("Some fields are empty, please try again")
            }
            else {
                if(viewModel.add(title, note, date)){
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
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(addTodoSuccess)
    }

    private fun snackbar(msg: String){
        Snackbar.make(requireActivity().findViewById(R.id.frameLayoutAddTodo),msg, Snackbar.LENGTH_LONG).show()
    }
}