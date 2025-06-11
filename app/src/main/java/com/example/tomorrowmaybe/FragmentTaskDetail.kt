package com.example.tomorrowmaybe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomorrowmaybe.databinding.FragmentTaskDetailBinding
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTaskDetail : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private val args: FragmentTaskDetailArgs by navArgs()
    private lateinit var currentTask: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTaskDetail_to_FirstFragment)
        }

        args.taskId?.let { viewModel.fetchTask(it) }

        viewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            currentTask = task
            binding.nombreEdit.setText(task.title)
            binding.descripcionEdit.setText(task.description)
            binding.fechaEdit.setText(task.dueDate)
        }

        binding.guardarEdit.setOnClickListener {
            currentTask.title = binding.nombreEdit.text.toString().trim()
            currentTask.description = binding.descripcionEdit.text.toString().trim()
            currentTask.dueDate = binding.fechaEdit.text.toString().trim()

            viewModel.updateTask(currentTask)
            Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.borrarEdit.setOnClickListener {
            viewModel.deleteTask(currentTask.id)
            Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
