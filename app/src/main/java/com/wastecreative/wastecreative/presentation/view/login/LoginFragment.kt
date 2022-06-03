package com.wastecreative.wastecreative.presentation.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentLoginBinding



class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)

        }
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }
        return binding.root
    }
}