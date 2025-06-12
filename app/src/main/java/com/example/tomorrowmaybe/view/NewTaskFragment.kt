package com.example.tomorrowmaybe.view
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomorrowmaybe.databinding.FragmentNewtaskBinding
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewtaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private val args: NewTaskFragmentArgs by navArgs()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var editingTask: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewtaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFechaSelector()
        setupBotones()
        observarErrores()

        args.taskId?.let { taskId ->
            viewModel.fetchTaskById(taskId)
            viewModel.selectedTask.observe(viewLifecycleOwner) { task ->
                editingTask = task
                binding.taskNameEditText.setText(task.name)
                binding.descripcionEdit.setText(task.description)
                binding.fechaEdit.setText(dateFormat.format(task.date))
            }
        }
    }

    private fun setupFechaSelector() {
        binding.fechaEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val fecha = String.format("%02d/%02d/%04d", d, m + 1, y)
                    binding.fechaEdit.setText(fecha)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun setupBotones() {
        // Guardar tarea
        binding.saveTaskButton.setOnClickListener {
            val nombre = binding.taskNameEditText.text.toString().trim()
            val descripcion = binding.descripcionEdit.text.toString().trim()
            val fechaTexto = binding.fechaEdit.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty() || fechaTexto.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = try {
                dateFormat.parse(fechaTexto)!!
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Fecha inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaTarea = editingTask?.copy(
                name = nombre,
                description = descripcion,
                date = fecha
            ) ?: Task(
                id = UUID.randomUUID().toString(),
                name = nombre,
                description = descripcion,
                date = fecha
            )

            if (editingTask != null) {
                viewModel.updateTask(nuevaTarea)
                Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addTask(nuevaTarea)
                Toast.makeText(requireContext(), "Tarea creada", Toast.LENGTH_SHORT).show()
            }

            findNavController().navigateUp()
        }

        // Botón regresar
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observarErrores() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
