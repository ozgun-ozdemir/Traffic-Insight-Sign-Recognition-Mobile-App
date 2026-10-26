package com.example.trafficsigns

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

// model input: 30x30 RGB, raw 0-255 values. output: 43 GTSRB classes
class SignClassifier(context: Context) : AutoCloseable {

    data class Result(val label: String, val confidence: Float)

    private val interpreter = Interpreter(loadModel(context))
    private val labels = context.assets.open(LABELS_FILE).bufferedReader().readLines()
    private val inputSize = interpreter.getInputTensor(0).shape()[1]

    fun classify(bitmap: Bitmap): Result {
        val output = Array(1) { FloatArray(labels.size) }
        interpreter.run(toInputBuffer(bitmap), output)

        val scores = output[0]
        val best = scores.indices.maxBy { scores[it] }
        return Result(labels[best], scores[best])
    }

    override fun close() = interpreter.close()

    private fun toInputBuffer(bitmap: Bitmap): ByteBuffer {
        val resized = bitmap.scale(inputSize, inputSize)
        val pixels = IntArray(inputSize * inputSize)
        resized.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        val buffer = ByteBuffer.allocateDirect(pixels.size * 3 * Float.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
        for (pixel in pixels) {
            buffer.putFloat(((pixel shr 16) and 0xFF).toFloat())
            buffer.putFloat(((pixel shr 8) and 0xFF).toFloat())
            buffer.putFloat((pixel and 0xFF).toFloat())
        }
        buffer.rewind()
        return buffer
    }

    private fun loadModel(context: Context): MappedByteBuffer =
        context.assets.openFd(MODEL_FILE).use { fd ->
            FileInputStream(fd.fileDescriptor).channel.use { channel ->
                channel.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength)
            }
        }

    companion object {
        private const val MODEL_FILE = "traffic_sign_model.tflite"
        private const val LABELS_FILE = "labels.txt"
    }
}
