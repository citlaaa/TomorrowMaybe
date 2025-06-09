package com.example.tomorrowmaybe.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.FragmentRegisterBinding
import com.example.tomorrowmaybe.viewModel.RegisterViewModel
import com.example.tomorrowmaybe.utils.FragmentCommunicator
import com.example.tomorrowmaybe.view.OnboardingActivity

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var isValid: Boolean = false
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var communicator: FragmentCommunicator



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        return binding.root

    }

    private fun setupView() {
        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btRegister.setOnClickListener {
            viewModel.requestSignUp(binding.tietEmail.text.toString(),
                binding.tietPassword.text.toString())
        }

        binding.tietName.addTextChangedListener {
            if (binding.tietName.text.toString().isEmpty()) {
                binding.tfName.error = "Por favor introduce tu nombre"
                isValid = false
            } else {
                isValid = true
            }
        }

        binding.tietEmail.addTextChangedListener {
            if (binding.tietEmail.text.toString().isEmpty()) {
                binding.tfEmail.error = "Por favor introduce un correo"
                isValid = false
            } else {
                isValid = true
            }
        }

        binding.tietPassword.addTextChangedListener {
            if (binding.tietPassword.text.toString().isEmpty()) {
                binding.tfPassword.error = "Por favor introduce una contraseña"
                isValid = false
            } else {
                isValid = true
            }
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.validRegister.observe(viewLifecycleOwner) { validRegister ->
            if (validRegister) {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(activity, "Ingresa todos los datos", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}