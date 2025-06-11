package com.example.tomorrowmaybe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tomorrowmaybe.databinding.FragmentSecondBinding
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

import android.app.DatePickerDialog
import java.util.Calendar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear la fecha como desees (ejemplo: "dd/MM/yyyy")
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.fechaEdit.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.saveTaskButton.setOnClickListener {
            val name = binding.taskNameEditText.text.toString().trim()
            val description = binding.descripcionEdit.text.toString().trim()
            val date = binding.fechaEdit.text.toString().trim()

            if (name.isEmpty() || description.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(title = name, description = description, dueDate = date)
            viewModel.addTask(task)

            Toast.makeText(requireContext(), "Tarea creada", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fechaEdit.setOnClickListener {
            showDatePicker()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
