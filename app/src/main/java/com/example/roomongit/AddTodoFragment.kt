package com.example.roomongit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.roomongit.databinding.InputFragmentLayoutBinding
import com.example.roomongit.dbnew.TodoFB

class AddTodoFragment(private val todo: TodoFB? = null): Fragment() {
    private lateinit var viewModel: TodoViewModel
    private lateinit var binding:InputFragmentLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        binding = InputFragmentLayoutBinding.bind(view)
        todo?.let {
            binding.titleInputField.setText(it.title)
            binding.noteInputField.setText(it.note)
            binding.dateInputField.setText(it.date)
        }
        binding.addButton.setOnClickListener {
            val title = binding.titleInputField.text.toString()
            val note = binding.noteInputField.text.toString()
            val date = binding.dateInputField.text.toString()
            viewModel.addTodo(title, note, date)
            parentFragmentManager.popBackStack()
        }
    }
}