package com.example.trafficsigns

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var resView: TextView
    private lateinit var imageView: ImageView
    private lateinit var classifier: SignClassifier
    private var bitmap: Bitmap? = null
    private var cameraImageUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { showImage(it) }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
            if (saved) cameraImageUri?.let { showImage(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resView = findViewById(R.id.resView)
        imageView = findViewById(R.id.imageView)
        classifier = SignClassifier(this)

        findViewById<Button>(R.id.selectBtn).setOnClickListener {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        findViewById<Button>(R.id.cameraBtn).setOnClickListener {
            val uri = createCameraImageUri()
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        }

        findViewById<Button>(R.id.predictBtn).setOnClickListener {
            val current = bitmap
            if (current == null) {
                resView.setText(R.string.no_image)
                return@setOnClickListener
            }
            val result = classifier.classify(current)
            resView.text = if (result.confidence < MIN_CONFIDENCE) {
                getString(R.string.not_recognized, result.confidence * 100)
            } else {
                getString(R.string.prediction_result, result.label, result.confidence * 100)
            }
        }
    }

    override fun onDestroy() {
        classifier.close()
        super.onDestroy()
    }

    private fun showImage(uri: Uri) {
        bitmap = loadBitmap(uri)
        imageView.setImageBitmap(bitmap)
        resView.setText(R.string.prediction_placeholder)
    }

    private fun loadBitmap(uri: Uri): Bitmap =
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri)) { decoder, _, _ ->
            // hardware bitmaps can't be read pixel by pixel
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }

    private fun createCameraImageUri(): Uri {
        val dir = File(cacheDir, "camera").apply { mkdirs() }
        val file = File(dir, "capture.jpg")
        return FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
    }

    companion object {
        private const val MIN_CONFIDENCE = 0.8f
    }
}
