package com.example.roomongit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AddTodoFragment: Fragment() {
    private lateinit var viewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        val naneInputField:EditText = view.findViewById(R.id.taskInputField)
        val positionInputField:EditText = view.findViewById(R.id.progressInputField)
        val addButton : Button = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val task = naneInputField.text.toString()
            val progress = positionInputField.text.toString()
            viewModel.addTodo(task, progress)
            parentFragmentManager.popBackStack()
        }
    }
}