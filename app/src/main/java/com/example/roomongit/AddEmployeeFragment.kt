package com.example.roomongit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

class AddEmployeeFragment: Fragment() {
    private lateinit var viewModel: EmployeeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EmployeeViewModel::class.java)
        val naneInputField:EditText = view.findViewById(R.id.nameInputField)
        val positionInputField:EditText = view.findViewById(R.id.positionInputField)
        val addButton : Button = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val name = naneInputField.text.toString()
            val position = positionInputField.text.toString()
            viewModel.addEmployee(name, position)
            parentFragmentManager.popBackStack()
        }
    }
}