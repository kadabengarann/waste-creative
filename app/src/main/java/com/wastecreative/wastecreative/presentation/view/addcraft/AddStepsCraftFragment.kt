package com.wastecreative.wastecreative.presentation.view.addcraft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentAddDetailCraftBinding
import com.wastecreative.wastecreative.databinding.FragmentAddStepsCraftBinding
import com.wastecreative.wastecreative.databinding.FragmentCraftBinding


class AddStepsCraftFragment : Fragment() {
    private var _binding: FragmentAddStepsCraftBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStepsCraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            uploadBtn.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_navigation_add_steps_craft_to_navigation_craft)
            }
                prevBtn.setOnClickListener{
                    activity?.onBackPressed()
                }
        }
    }
}