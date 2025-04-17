package com.eugene.signdetect.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.io.File

class FrameAnalyzer(
    private val context: Context,
    private val onFrameCaptured: (ByteArray) -> Unit
) : ImageAnalysis.Analyzer {

    private var lastProcessedTime = 0L
    private val intervalMs = 1000L

    override fun analyze(image: ImageProxy) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastProcessedTime >= intervalMs) {
            lastProcessedTime = currentTime
            val byteArray = imageToByteArray(image)
            onFrameCaptured(byteArray)
            val debugFile = File(context.cacheDir, "debug_frame.jpg")
            debugFile.writeBytes(byteArray)
        }

        image.close()
    }

    private fun imageToByteArray(image: ImageProxy): ByteArray {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
        val jpegBytes = out.toByteArray()

        // Преобразуем в Bitmap
        val bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)

        // Поворачиваем в нужную ориентацию (обычно 90° для задней камеры)
        val matrix = Matrix()
        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat()) // авто-поворот

        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Преобразуем обратно в JPEG байты
        val rotatedOut = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, rotatedOut)
        return rotatedOut.toByteArray()
    }
}