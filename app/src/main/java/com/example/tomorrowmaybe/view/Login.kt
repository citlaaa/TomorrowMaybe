package com.example.tomorrowmaybe.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.FragmentLoginBinding
import com.example.tomorrowmaybe.utils.FragmentCommunicator
import com.example.tomorrowmaybe.viewModel.LoginViewModel
import com.example.tomorrowmaybe.ListActivity

class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private var isValid: Boolean = false
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.button.setOnClickListener {
            if(isValid) {
                requestLogin()
            } else {
                Toast.makeText(activity, "Datos Invalidos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.correoL.addTextChangedListener {
            isValid = binding.correoL.text.toString().isNotEmpty()
            if(!isValid) binding.correoL.error = "Campo requerido"
        }

        binding.contraseAL.addTextChangedListener {
            isValid = binding.contraseAL.text.toString().isNotEmpty()
            if(!isValid) binding.contraseAL.error = "Campo requerido"
        }

        binding.textView3.setOnClickListener {
            findNavController().navigate(R.id.action_login2_to_register2)
        }
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.sessionValid.observe(viewLifecycleOwner) { sessionValid ->
            if(sessionValid) {
                startActivity(Intent(activity, ListActivity::class.java))
                activity?.finish()
            } else {
                Toast.makeText(activity, "Ingreso invalido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLogin() {
        viewModel.requestSingnIn(
            binding.correoL.text.toString(),
            binding.contraseAL.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}