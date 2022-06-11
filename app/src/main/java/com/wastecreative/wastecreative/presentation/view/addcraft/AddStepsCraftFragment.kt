package com.wastecreative.wastecreative.presentation.view.addcraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentAddStepsCraftBinding


class AddStepsCraftFragment : Fragment() {
    private var _binding: FragmentAddStepsCraftBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddCraftViewModel by activityViewModels()

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
        observe()
    }

    private fun observe() {
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
        binding.apply {
            uploadBtn.setOnClickListener {
                submitForm()
            }
                prevBtn.setOnClickListener{
                    activity?.onBackPressed()
                }
        }
    }

    private fun submitForm() {
        val materials = binding.craftMaterialInput.text.toString()
        val steps = binding.craftStepsInput.text.toString()
        val video = binding.craftVidLinkInput.text.toString()
        if (validate()){
            viewModel.setStepsCraft(materials, steps, video)

        }else{
            Toast.makeText(requireContext(),"Please fill all required field", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validateMaterials():Boolean{
        val materials = binding.craftMaterialInput.text.toString().trim{it<=' '}.isEmpty()
        return if (materials) {
            binding.craftMaterialsEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.craftMaterialsEditTextLayout.helperText = null
            true
        }
    }
    private fun validateSteps():Boolean{
        val steps = binding.craftStepsInput.text.toString().trim{it<=' '}.isEmpty()
        return if (steps) {
            binding.craftStepsEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.craftStepsEditTextLayout.helperText = null
            true
        }
    }

    private fun validate(): Boolean = validateMaterials() && validateSteps()

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