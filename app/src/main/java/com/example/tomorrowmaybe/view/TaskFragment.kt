package com.example.tomorrowmaybe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.FragmentTaskBinding
import com.example.tomorrowmaybe.view.adapter.TaskAdapter
import com.example.tomorrowmaybe.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var tasksAdapter: TaskAdapter

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

        // Inicializa el adaptador
        tasksAdapter = TaskAdapter(
            tasks = mutableListOf(),
            onItemClick = { task ->
                // Navegar a detalle de tarea (si deseas implementar)
                val action = TaskFragmentDirections
                    .actionTaskFragmentToTaskDetailFragment(task.id)
                findNavController().navigate(action)
            },
            onDeleteClick = { task ->
                viewModel.deleteTask(task.id)
            },
            onEditClick = { task ->
                val action = TaskFragmentDirections
                    .actionTaskFragmentToNewTaskFragment(task.id) // <-- Si envías el ID para editar
                findNavController().navigate(action)
            }
        )

        // Configura el RecyclerView
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
            setHasFixedSize(true)
        }

        // Botón para agregar nueva tarea
        binding.fabAddTask.setOnClickListener {
            val action = TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(null)
            findNavController().navigate(action)
        }

        // Observadores
        viewModel.tasks.observe(viewLifecycleOwner) { lista ->
            tasksAdapter.updateTasks(lista)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // Puedes mostrar un ProgressBar si tienes uno
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar tareas
        viewModel.fetchTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

