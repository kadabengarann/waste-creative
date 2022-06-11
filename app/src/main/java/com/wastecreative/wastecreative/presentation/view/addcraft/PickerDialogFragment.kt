package com.wastecreative.wastecreative.presentation.view.addcraft

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wastecreative.wastecreative.databinding.FragmentPickerDialogBinding
import com.wastecreative.wastecreative.utils.createTempFile
import com.wastecreative.wastecreative.utils.uriToFile
import java.io.File

class PickerDialogFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentPickerDialogBinding? = null
    private val viewModel: AddCraftViewModel by activityViewModels()

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val binding get() = _binding!!
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                startTakePhoto()
            } else {
//                showError(getString(R.string.permission_failed))
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickerDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupViewModel()
        setupAction()
        return root
    }

    private fun setupViewModel() {

    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun setupAction() {
        binding.touchLogout.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA)) {
                startTakePhoto()
            } else {
                requestPermissionLauncher.launch(
                    CAMERA_PERMISSIONS
                )
            }
        }
        binding.touchLang.setOnClickListener {
            startGallery()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose image")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            viewModel.setImage(getFile)
            dismiss()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.let {
            intent.resolveActivity(it.packageManager)
        }
        createTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.wastecreative.wastecreative",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            viewModel.setImage(getFile)
            dismiss()
        }
    }

    companion object{
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}