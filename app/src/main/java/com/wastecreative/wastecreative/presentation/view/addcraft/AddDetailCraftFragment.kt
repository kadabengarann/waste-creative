package com.wastecreative.wastecreative.presentation.view.addcraft

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentAddDetailCraftBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import java.io.File


class AddDetailCraftFragment : Fragment() {
    private var _binding: FragmentAddDetailCraftBinding? = null
    private val binding get() = _binding!!

    private val factory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val addCraftViewModel: AddCraftViewModel by activityViewModels {
        factory
    }

    override fun onDestroy() {
        super.onDestroy()

        addCraftViewModel.clearData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddDetailCraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setupAction()
        observeData()
    }

    private fun observeData() {
        addCraftViewModel.image.observe(viewLifecycleOwner){
            processImage(it)
        }
    }

    private fun processImage(file: File?) {
        if (file != null){
            val result = BitmapFactory.decodeFile(file.path)
            binding.imgPrev.setImageBitmap(result)
            binding.addCraftPhoto.visibility = View.GONE
            binding.grPreview.visibility = View.VISIBLE
        }else {
            binding.addCraftPhoto.visibility = View.VISIBLE
            binding.grPreview.visibility = View.GONE
        }
    }

    private fun setupAction() {
        binding.apply {
            nextBtn.setOnClickListener {
                submitForm()
            }
            addCraftPhoto.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_add_detail_craft_to_pickerDialogFragment)
            }
            changeImgBtn.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_add_detail_craft_to_pickerDialogFragment)
            }
        }
    }

    private fun submitForm() {
        val name = binding.craftNameInput.text.toString()
        val desc = binding.craftDescInput.text.toString()
        if (validate()){
            addCraftViewModel.setDetailCraft(name, desc)
            view?.findNavController()?.navigate(R.id.action_navigation_add_detail_craft_to_navigation_add_steps_craft)
        }else{
            Toast.makeText(requireContext(),"Please fill all required field", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validatePhoto():Boolean{
        val isPhoto = addCraftViewModel.image.value
        return if (isPhoto == null) {
            Toast.makeText(requireContext(),"Please choose image!", Toast.LENGTH_SHORT).show()
            false
        }else
            true

    }
    private fun validateName():Boolean{
        val name = binding.craftNameInput.text.toString().trim{it<=' '}.isEmpty()
        return if (name) {
            binding.craftNameEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.craftNameEditTextLayout.helperText = null
            true
        }
    }
    private fun validateDesc():Boolean{
        val desc = binding.craftDescInput.text.toString().trim{it<=' '}.isEmpty()
        return if (desc) {
            binding.craftDescEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.craftDescEditTextLayout.helperText = null
            true
        }
    }

    private fun validate(): Boolean = validateName() && validateDesc() && validatePhoto()

}