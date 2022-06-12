package com.wastecreative.wastecreative.presentation.view.addcraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentAddStepsCraftBinding
import com.wastecreative.wastecreative.presentation.adapter.InputListAdapter


class AddStepsCraftFragment : Fragment() {
    private var _binding: FragmentAddStepsCraftBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddCraftViewModel by activityViewModels()
    private val inputListAdapter1: InputListAdapter by lazy {
        InputListAdapter(
            arrayListOf()
        )
    }

    private val inputListAdapter2: InputListAdapter by lazy {
        InputListAdapter(
            arrayListOf()
        )
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
        binding.rvTools.apply {
            layoutManager = LinearLayoutManager(context)
            isVerticalScrollBarEnabled = true
            setHasFixedSize(true)
            adapter = inputListAdapter1
        }
        binding.rvSteps.apply {
            layoutManager = LinearLayoutManager(context)
            isVerticalScrollBarEnabled = true
            setHasFixedSize(true)
            adapter = inputListAdapter2
        }
        setupAction()
        observe()
    }

    private fun observe() {
        inputListAdapter1.setData(viewModel._tools)
        inputListAdapter2.setData(viewModel._steps)

        viewModel.uploadResult.observe(viewLifecycleOwner){ response ->
            if (response != null) when (response) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "getString(R.string.success_upload_story)",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.clearData()
                    view?.findNavController()?.navigate(R.id.action_navigation_add_steps_craft_to_navigation_craft)
                }
                is Result.Error -> {
                    showLoading(false)
                    showError(response.error)
                }
            }
        }
    }

    private fun setupAction() {
        validateToolsInput()
        validateStepsInput()
        binding.apply {
            uploadBtn.setOnClickListener {
                submitForm()
            }
                prevBtn.setOnClickListener{
                    activity?.onBackPressed()
                }
            craftToolsInput.doOnTextChanged { _, _, _, _ ->
                validateToolsInput()
            }
            craftStepsInput.doOnTextChanged { _, _, _, _ ->
                validateStepsInput()
            }
            addToolsBtn.setOnClickListener {
                val item =craftToolsInput.text.toString()
                inputListAdapter1.addList(item)
                viewModel.setTools(inputListAdapter1.getData())
                viewModel._tools.addAll(inputListAdapter1.getData())
                binding.craftToolsInput.text = null
            }
            addStepsBtn.setOnClickListener {
                val item =craftStepsInput.text.toString()
                inputListAdapter2.addList(item)
                viewModel.setSteps(inputListAdapter2.getData())
                viewModel._steps.addAll(inputListAdapter2.getData())
                binding.craftStepsInput.text = null
            }
        }
    }

    private fun submitForm() {
        if (validate()){
            viewModel.uploadCraft()
        }else{
            Toast.makeText(requireContext(),"Please fill all required field", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validateStepsInput():Boolean{
        val item = binding.craftStepsInput.text.toString().trim{it<=' '}.isEmpty()
        return if (item) {
            binding.addStepsBtn.isEnabled = false
            binding.addStepsBtn.isClickable = false
            false
        }else {
            binding.addStepsBtn.isEnabled = true
            binding.addStepsBtn.isClickable = true
            true
        }
    }
    private fun validateSteps():Boolean{
        val mats = viewModel._steps.isEmpty()
        return !mats
    }
    private fun validateToolsInput():Boolean{
        val item = binding.craftToolsInput.text.toString().trim{it<=' '}.isEmpty()
        return if (item) {
            binding.addToolsBtn.isEnabled = false
            binding.addToolsBtn.isClickable = false
            false
        }else {
            binding.addToolsBtn.isEnabled = true
            binding.addToolsBtn.isClickable = true
            true
        }
    }
    private fun validateTools():Boolean{
        val mats = viewModel._tools.isEmpty()
        return !mats
    }

    private fun validate(): Boolean = validateTools() && validateSteps()

    private fun showLoading(b: Boolean) {
        binding.incProgress.progressOverlay.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun showError(error: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Failed")
            setMessage(error)
            setNegativeButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}