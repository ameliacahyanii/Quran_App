package com.setiadev.quran.presentation.quran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiadev.quran.adapter.ListSurahAdapter
import com.setiadev.quran.databinding.FragmentQuranBinding

class QuranFragment : Fragment() {
    private var _binding : FragmentQuranBinding? = null
    private val binding get() = _binding as FragmentQuranBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuranBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        viewModel.getListSurah()

        viewModel.listSurah.observe(viewLifecycleOwner) {
            binding.rvQuran.apply {
                val mAdapter = ListSurahAdapter()
                mAdapter.setData(it.listSurah)
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}