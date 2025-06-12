package com.example.tomorrowmaybe.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tomorrowmaybe.databinding.FragmentTaskDetailBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private val args: TaskDetailFragmentArgs by navArgs()


    private lateinit var currentTask: Task
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFechaPicker()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        args.taskId?.let { viewModel.fetchTaskById(it) }

        viewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            currentTask = task
            binding.nombreEdit.setText(task.name)
            binding.descripcionEdit.setText(task.description)
            binding.fechaEdit.setText(dateFormat.format(task.date))
        }

        binding.guardarEdit.setOnClickListener {
            val nombre = binding.nombreEdit.text.toString().trim()
            val descripcion = binding.descripcionEdit.text.toString().trim()
            val fechaTexto = binding.fechaEdit.text.toString().trim()

            if (nombre.isBlank() || descripcion.isBlank() || fechaTexto.isBlank()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = try {
                dateFormat.parse(fechaTexto)
            } catch (e: Exception) {
                null
            }

            if (fecha == null) {
                Toast.makeText(requireContext(), "Fecha inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tareaActualizada = currentTask.copy(
                name = nombre,
                description = descripcion,
                date = fecha
            )

            viewModel.updateTask(tareaActualizada)
            Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.borrarEdit.setOnClickListener {
            viewModel.deleteTask(currentTask.id)
            Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupFechaPicker() {
        binding.fechaEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val selectedDate = "%02d/%02d/%04d".format(d, m + 1, y)
                    binding.fechaEdit.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
