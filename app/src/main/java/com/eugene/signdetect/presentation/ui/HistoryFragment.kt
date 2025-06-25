package com.eugene.signdetect.presentation.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugene.signdetect.R
import com.eugene.signdetect.databinding.FragmentHistoryBinding
import com.eugene.signdetect.presentation.ui.adapter.HistoryAdapter
import com.eugene.signdetect.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels<HistoryViewModel>()
    private val adapter = HistoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHistoryBinding.bind(view)

        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var logoutButton = view.findViewById<ImageButton>(R.id.logout)
        logoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_cameraFragment)
        }
        viewModel.historyLiveData.observe(viewLifecycleOwner) { historyList ->
            adapter.submitList(historyList)
        }
        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "") ?: ""
        viewModel.loadHistory(token)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}