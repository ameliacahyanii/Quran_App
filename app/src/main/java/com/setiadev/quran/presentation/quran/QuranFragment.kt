package com.setiadev.quran.presentation.quran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiadev.quran.adapter.QuranAdapter
import com.setiadev.quran.databinding.FragmentQuranBinding
import com.setiadev.quran.network.Resource
import com.setiadev.quran.presentation.ViewModelFactory

class QuranFragment : Fragment() {
    private var _binding : FragmentQuranBinding? = null
    private val binding get() = _binding as FragmentQuranBinding

    private val quranViewModel: QuranViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuranBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quranViewModel.getListSurah().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    binding.rvQuran.apply {
                        val mAdapter = QuranAdapter()
                        mAdapter.setData(it.data)
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                    showLoading(false)
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                rvQuran.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                rvQuran.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}