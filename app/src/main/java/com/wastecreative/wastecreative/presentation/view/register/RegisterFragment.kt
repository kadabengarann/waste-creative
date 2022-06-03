package com.wastecreative.wastecreative.presentation.view.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }
        binding.registerLogin.setOnClickListener {

        }
        return binding.root


    }


}