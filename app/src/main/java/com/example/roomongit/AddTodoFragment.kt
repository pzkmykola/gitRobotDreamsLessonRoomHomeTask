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
        val titleInputField:EditText = view.findViewById(R.id.titleInputField)
        val noteInputField:EditText = view.findViewById(R.id.noteInputField)
        val dateInputField:EditText = view.findViewById(R.id.dateInputField)
        val addButton:Button = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val title = titleInputField.text.toString()
            val note = noteInputField.text.toString()
            val date = dateInputField.text.toString()
            viewModel.addTodo(title, note, date)
            parentFragmentManager.popBackStack()
        }
    }
}