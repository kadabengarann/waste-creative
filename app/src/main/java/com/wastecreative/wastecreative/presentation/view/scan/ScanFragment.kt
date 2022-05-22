package com.wastecreative.wastecreative.presentation.view.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentScanBinding
import com.wastecreative.wastecreative.utils.createFile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanFragment : Fragment() {
    private var binding: FragmentScanBinding? = null
    private var cameraExecutor: ExecutorService? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                startCamera()
                showCameraUI(true)
            } else {
                showCameraUI(false)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_failed),
                    Toast.LENGTH_SHORT
                ).show()
                // create snackbar to appSetting
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(
                REQUIRED_PERMISSIONS
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        if (checkPermission(Manifest.permission.CAMERA)) {
            startCamera()
            showCameraUI(true)
        } else {
            showCameraUI(false)
        }
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =  if (Build.VERSION.SDK_INT >= 23) requireContext().getColor(android.R.color.transparent) else requireContext().resources.getColor(android.R.color.transparent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupAction() {
        binding?.apply {
            backBtn.setOnClickListener{
                activity?.onBackPressed()
            }
            selectFromGallery.setOnClickListener {
                startGallery()
            }
            shutterImage.setOnClickListener {
                shutter()
            }
            turnOnCamera.setOnClickListener {
                requestPermissionLauncher.launch(
                    REQUIRED_PERMISSIONS
                )
            }
        }
    }

    private fun openAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun showCameraUI(bool: Boolean){
        binding?.apply {
            grCameraDisabled.visibility = if (bool) View.GONE else View.VISIBLE
            grCameraUi.visibility = if (bool) View.VISIBLE else View.GONE
        }
    }
    private fun shutter() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Photo capture failed: ${exc.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val toScanResultFragment = ScanFragmentDirections.actionNavigationCameraToNavigationScanResult(savedUri)
                    view?.findNavController()?.navigate(toScanResultFragment)
                }
            }
        )
    }

    override fun onDestroyView() {
        binding = null
        cameraExecutor?.shutdown()
        cameraExecutor = null
        super.onDestroyView()
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        binding?.apply {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(scanPreview.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder().build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, ContextCompat.getMainExecutor(requireContext()))

        }
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
            val toScanResultFragment = ScanFragmentDirections.actionNavigationCameraToNavigationScanResult(selectedImg)
            view?.findNavController()?.navigate(toScanResultFragment)
        }
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}