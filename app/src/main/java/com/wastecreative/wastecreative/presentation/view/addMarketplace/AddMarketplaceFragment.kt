package com.wastecreative.wastecreative.presentation.view.addMarketplace

import android.graphics.BitmapFactory
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
import com.wastecreative.wastecreative.databinding.FragmentAddMarketplaceBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.view.addcraft.AddStepsCraftFragmentDirections
import java.io.File


class AddMarketplaceFragment : Fragment() {
    private var _binding: FragmentAddMarketplaceBinding? = null
    private val binding get() = _binding!!

    private val factory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val addPostViewModel: AddMarketplaceViewModel by activityViewModels {
        factory
    }

    override fun onDestroy() {
        super.onDestroy()

        addPostViewModel.clearData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddMarketplaceBinding.inflate(inflater, container, false)
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
        addPostViewModel.image.observe(viewLifecycleOwner){
            processImage(it)
        }
        addPostViewModel.uploadResult.observe(viewLifecycleOwner){ response ->
            if (response != null) when (response) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.success),
                        Toast.LENGTH_SHORT
                    ).show()
                    addPostViewModel.clearData()
                    val toListPage =
                        AddMarketplaceFragmentDirections.actionNavigationAddMarketplaceToNavigationMarketplace()
                    toListPage.reFetch = true
                    view?.findNavController()?.navigate(toListPage)
                }
                is Result.Error -> {
                    showLoading(false)
                    showError(response.error)
                }
            }
        }
    }
    private fun processImage(file: File?) {
        if (file != null){
            val result = BitmapFactory.decodeFile(file.path)
            binding.imgPrev.setImageBitmap(result)
            binding.addPostPhoto.visibility = View.GONE
            binding.grPreview.visibility = View.VISIBLE
        }else {
            binding.addPostPhoto.visibility = View.VISIBLE
            binding.grPreview.visibility = View.GONE
        }
    }

    private fun setupAction() {
        binding.apply {
            nextBtn.setOnClickListener {
                submitForm()
            }
            addPostPhoto.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_add_marketplace_to_post_pickerDialogFragment)
            }
            changeImgBtn.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_add_marketplace_to_post_pickerDialogFragment)
            }
        }
    }

    private fun submitForm() {
        val name = binding.postNameInput.text.toString()
        val desc = binding.postDescInput.text.toString()
        val priceInput = binding.postPriceInput.text.toString()
        var price =0
        if (!priceInput.trim{it<=' '}.isEmpty())
            price  =  priceInput.toInt()
        val addr = binding.postAddressInput.text.toString()
        if (validate()){
            addPostViewModel.submitDetailPost(name, desc, price, addr)
        }else{
            Toast.makeText(requireContext(),"Please fill all required field", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validatePhoto():Boolean{
        val isPhoto = addPostViewModel.image.value
        return if (isPhoto == null) {
            Toast.makeText(requireContext(),"Please choose image!", Toast.LENGTH_SHORT).show()
            false
        }else
            true

    }
    private fun validateName():Boolean{
        val name = binding.postNameInput.text.toString().trim{it<=' '}.isEmpty()
        return if (name) {
            binding.postNameEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.postNameEditTextLayout.helperText = null
            true
        }
    }
    private fun validateDesc():Boolean{
        val desc = binding.postDescInput.text.toString().trim{it<=' '}.isEmpty()
        return if (desc) {
            binding.postDescEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.postDescEditTextLayout.helperText = null
            true
        }
    }
    private fun validatePrice():Boolean{
        val desc = binding.postPriceInput.text.toString().trim{it<=' '}.isEmpty()
        return if (desc) {
            binding.postPriceEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.postPriceEditTextLayout.helperText = null
            true
        }
    }
    private fun validateAddress():Boolean{
        val desc = binding.postAddressInput.text.toString().trim{it<=' '}.isEmpty()
        return if (desc) {
            binding.postAddressEditTextLayout.helperText = "Please fill this field!"
            false
        }else {
            binding.postAddressEditTextLayout.helperText = null
            true
        }
    }

    private fun validate(): Boolean = validateName() && validateDesc() && validatePrice() && validateAddress() && validatePhoto()

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