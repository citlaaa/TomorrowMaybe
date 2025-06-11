package com.example.tomorrowmaybe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.FragmentTaskBinding
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.view.adapter.TasksAdapter
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        viewModel.fetchTasks()
    }

    private fun setupRecyclerView() {
        tasksAdapter = TasksAdapter(emptyList()) { task ->
            // Aquí puedes manejar el clic en una tarea si lo necesitas
        }

        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasks?.let {
                tasksAdapter.updateTasks(it)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // Aquí puedes manejar el estado de carga si lo deseas
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                // Mostrar error al usuario
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}