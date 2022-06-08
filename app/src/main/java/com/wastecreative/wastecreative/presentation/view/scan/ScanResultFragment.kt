package com.wastecreative.wastecreative.presentation.view.scan

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.DetectionResult
import com.wastecreative.wastecreative.databinding.FragmentScanResultBinding
import com.wastecreative.wastecreative.presentation.adapter.DetectedWasteListAdapter
import com.wastecreative.wastecreative.utils.getColorFromAttr
import com.wastecreative.wastecreative.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.File

class ScanResultFragment : Fragment() {
    private var binding: FragmentScanResultBinding? = null
    private val data = ArrayList<String>()
    private val detectedWasteListAdapter: DetectedWasteListAdapter by lazy {
        DetectedWasteListAdapter(
            arrayListOf()
        )
    }
    private lateinit var resultScanIv: ImageView
    private lateinit var dataImgUri: Uri
    private var getFile: File? = null
    private var isCamera: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanResultBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        requireActivity().window.statusBarColor = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataImgUri = ScanResultFragmentArgs.fromBundle(arguments as Bundle).imgURL
        isCamera = ScanResultFragmentArgs.fromBundle(arguments as Bundle).isCamera

        binding?.apply {
            seeRecommBtn.setOnClickListener{
                Log.d("TAGAGAGAGAGGA", "onViewCreated: $isCamera")
                if (isCamera as Boolean){
                    if (getFile?.exists() == true) {
//                        try {
//                            getFile?.delete()
//                            Log.d("TAGAGAGAGAGGA", "onViewCreated: DELETE")
//                        }
//                        catch (exception: Exception){
//                            Log.d("TAGAGAGAGAGGA", "onViewCreated: NOT DELETE")
//                        }
                    }
                    else
                        Log.d("TAGAGAGAGAGGA", "onViewCreated: NOT EXIST")
                }
                view.findNavController().navigate(R.id.action_navigation_scan_result_to_navigation_craft_recommendation)
            }
        }
        setViewAndDetect(getBitmapImg())
        showRecyclerList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun showRecyclerList() {
        binding?.rvObjectResult?.apply {
            layoutManager = LinearLayoutManager(context)
            isVerticalScrollBarEnabled = true
            setHasFixedSize(true)
            adapter = detectedWasteListAdapter
        }
    }

    private fun getBitmapImg(): Bitmap {

        val myFile = uriToFile(dataImgUri, requireContext())
        getFile = myFile
        return BitmapFactory.decodeFile(getFile?.path)
    }

    private fun setViewAndDetect(bitmap: Bitmap) {
        binding?.apply {
            imgScanResult.setImageBitmap(bitmap)
            resultScanIv = imgScanResult
        }
        lifecycleScope.launch(Dispatchers.Default) { runObjectDetection(bitmap) }
    }

    private fun runObjectDetection(bitmap: Bitmap) {
        val image = TensorImage.fromBitmap(bitmap)
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(5)
            .setScoreThreshold(0.5f)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(
            requireContext(),
            "wastecreative.tflite",
            options
        )
        val results = detector.detect(image)

        val resultProcess = results.map {
            val category = it.categories.first()
            val text = "${category.label}, ${category.score.times(100).toInt()}%"
            data.add(category.label)
            DetectionResult(it.boundingBox, text)
        }
        val imgWithResult = drawDetectionResult(bitmap, resultProcess)
        activity?.runOnUiThread {
            resultScanIv.setImageBitmap(imgWithResult)
            detectedWasteListAdapter.setData(data)
        }
    }
    private fun drawDetectionResult(
        bitmap: Bitmap,
        detectionResults: List<DetectionResult>
    ): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        detectionResults.forEach {
            // draw bounding box
            pen.color = Color.RED
            pen.strokeWidth = 8F
            pen.style = Paint.Style.STROKE
            val box = it.boundingBox
            canvas.drawRect(box, pen)


            val tagSize = Rect(0, 0, 0, 0)

            // calculate the right font size
            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 2F

            pen.textSize = MAX_FONT_SIZE
            pen.getTextBounds(it.text, 0, it.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) pen.textSize = fontSize

            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
            canvas.drawText(
                it.text, box.left + margin,
                box.top + tagSize.height().times(1F), pen
            )
        }
        return outputBitmap
    }
    companion object {
        private const val MAX_FONT_SIZE = 96F
    }
}
