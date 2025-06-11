package com.example.tomorrowmaybe

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.FragmentTaskDetailBinding
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class FragmentTaskDetail : Fragment() {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private val args: FragmentTaskDetailArgs by navArgs()
    private var currentTask: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar la tarea existente o preparar para nueva
        args.taskId?.let { taskId ->
            viewModel.getTaskById(taskId).observe(viewLifecycleOwner) { task ->
                task?.let {
                    currentTask = it
                    populateTaskData(it)
                }
            }
        }

        setupListeners()
    }

    private fun populateTaskData(task: Task) {
        with(binding) {
            taskTitleEditText.setText(task.title)
            taskDescriptionEditText.setText(task.description)
            taskDateEditText.setText(task.dueDate)
        }
    }

    private fun setupListeners() {
        with(binding) {
            taskDateEditText.setOnClickListener { showDatePicker() }

            saveButton.setOnClickListener { saveTask() }

            deleteButton.setOnClickListener { deleteTask() }

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.taskDateEditText.setText(dateFormat.format(selectedDate.time))
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun saveTask() {
        with(binding) {
            val title = taskTitleEditText.text.toString()
            val description = taskDescriptionEditText.text.toString()
            val dueDate = taskDateEditText.text.toString()

            if (validateInput(title, description, dueDate)) {
                val task = currentTask?.copy(
                    title = title,
                    description = description,
                    dueDate = dueDate
                ) ?: Task(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    isCompleted = false
                )

                viewModel.saveTask(task)
                findNavController().navigateUp()
            }
        }
    }

    private fun deleteTask() {
        currentTask?.let {
            viewModel.deleteTask(it)
            findNavController().navigateUp()
        }
    }

    private fun validateInput(title: String, description: String, dueDate: String): Boolean {
        var isValid = true

        with(binding) {
            if (title.isEmpty()) {
                taskTitleLayout.error = getString(R.string.error_field_required)
                isValid = false
            } else {
                taskTitleLayout.error = null
            }

            if (description.isEmpty()) {
                taskDescriptionLayout.error = getString(R.string.error_field_required)
                isValid = false
            } else {
                taskDescriptionLayout.error = null
            }

            if (dueDate.isEmpty()) {
                taskDateLayout.error = getString(R.string.error_field_required)
                isValid = false
            } else {
                taskDateLayout.error = null
            }
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}