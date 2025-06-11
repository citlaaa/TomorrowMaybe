package com.example.tomorrowmaybe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.FragmentRegisterBinding
import com.example.tomorrowmaybe.utils.FragmentCommunicator
import com.example.tomorrowmaybe.viewModel.RegisterViewModel

class Register : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private var isValid: Boolean = false
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
        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.action_register2_to_login2)
        }

        binding.button2.setOnClickListener {
            if(isValid) {
                viewModel.requestSignUp(
                    binding.correoR.text.toString(),
                    binding.contrasenaR.text.toString()
                )
            }
        }

        binding.nombreR.addTextChangedListener {
            isValid = binding.nombreR.text.toString().isNotEmpty()
            if(!isValid) binding.textInputLayout3.error = "Campo requerido"
        }

        binding.correoR.addTextChangedListener {
            isValid = binding.correoR.text.toString().isNotEmpty()
            if(!isValid) binding.textInputLayout4.error = "Campo requerido"
        }

        binding.contrasenaR.addTextChangedListener {
            isValid = binding.contrasenaR.text.toString().isNotEmpty()
            if(!isValid) binding.textInputLayout5.error = "Campo requerido"
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.validRegister.observe(viewLifecycleOwner) { validRegister ->
            if(validRegister) {
                findNavController().navigate(R.id.action_register2_to_login2)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}