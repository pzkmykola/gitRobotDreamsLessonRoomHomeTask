package com.example.roomongit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.roomongit.databinding.InputFragmentLayoutBinding
import com.example.roomongit.dbnew.TodoFB
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddTodoFragment(private val todo: TodoFB? = null): BottomSheetDialogFragment() {
    @Inject
    lateinit var viewModel: TodoViewModel

    private lateinit var binding:InputFragmentLayoutBinding
    private var addTodoSuccess: Boolean = false
    private var isValid = true
    //val viewModel: TodoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = InputFragmentLayoutBinding.bind(view)
        todo?.let {
            binding.todoTitle.setText(it.title)
            binding.todoDescription.setText(it.note)
            binding.dateDropdown.setText(it.date)
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
            val title = binding.todoTitle.text.toString()
            val note = binding.todoDescription.text.toString()
            val date = binding.dateDropdown.text.toString()
            viewModel.addTodo(title, note, date)
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment())
                .commit()
        }
    }
}