package com.eugene.signdetect.presentation.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.eugene.signdetect.presentation.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import android.Manifest
import android.content.Context
import android.media.SoundPool
import android.widget.ImageButton
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eugene.signdetect.R
import com.eugene.signdetect.databinding.FragmentCameraBinding
import com.eugene.signdetect.presentation.util.SignName
import com.eugene.signdetect.presentation.viewmodel.AuthViewModel


@AndroidEntryPoint
class CameraFragment : Fragment(R.layout.fragment_camera) {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CameraViewModel by viewModels<CameraViewModel>()
    private val cameraExecutor by lazy { Executors.newSingleThreadExecutor() }
    private lateinit var sp: SoundPool
    private var soundId: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sp = SoundPool.Builder()
            .setMaxStreams(10)
            .build()
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestCameraPermission()
        }
        startCamera()
        var logoutButton = view.findViewById<ImageButton>(R.id.logout)
        logoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_camFragment_to_logFragment)
        }
        var toHistoryButton = view.findViewById<ImageButton>(R.id.history_button)
        toHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_historyFragment)
        }
        var lastNotificationTime = 0L
        val notificationIntervalMs = 2500L
        soundId = sp.load(requireContext(), R.raw.sound1, 1)
        viewModel.detectionResult.observe(viewLifecycleOwner) { results ->
            val listString = results.map { SignName.getName(it.label) }.distinct()
            val currentTime = System.currentTimeMillis()

            if (listString.isNotEmpty() && currentTime - lastNotificationTime >= notificationIntervalMs) {
                lastNotificationTime = currentTime
                sp.play(soundId, 1f, 1f, 0, 0, 1f)
                NoticeFragment(listString.joinToString(separator = "\n  \n")).show(
                    parentFragmentManager,
                    "dialog"
                )
            }
        }

    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Камера не разрешена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, FrameAnalyzer(requireContext()) { bytes ->
                        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                        val token = prefs.getString("token", "") ?: ""
                        viewModel.detect(bytes,token)
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}