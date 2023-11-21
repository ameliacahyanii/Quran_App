package com.setiadev.quran.presentation.adzan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.setiadev.quran.databinding.FragmentAdzanBinding
import com.setiadev.quran.network.Resource
import com.setiadev.quran.presentation.ViewModelFactory

class AdzanFragment : Fragment() {
    private var _binding: FragmentAdzanBinding? = null
    private val binding get() = _binding as FragmentAdzanBinding

    private val adzanViewModel: AdzanViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdzanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adzanViewModel.getDailyAdzanTime().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.apply {
                        it.data?.let { adzanDataResult ->
                            tvLocation.text = adzanDataResult.listLocation[1]
                            when (val listCity = adzanDataResult.listCity) {
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    val idCity = listCity.data?.get(0)?.id
                                    val cityName = listCity.data?.get(0)?.lokasi
                                    Toast.makeText(
                                        context,
                                        "id city: $idCity. $cityName",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                is Resource.Error -> {
                                    Toast.makeText(context, listCity.message, Toast.LENGTH_SHORT)
                                        .show()
                                    Log.e(
                                        "AdzanFragment",
                                        "getCity Id & Location: ${listCity.message}",
                                    )
                                }
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(context, "Error Update Your Location:\n" + it.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(
                        "AdzanFragment",
                        "Error when updating your location: ${it.message}",
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}