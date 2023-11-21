package com.setiadev.quran.presentation.quran

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiadev.quran.R
import com.setiadev.quran.adapter.SurahAdapter
import com.setiadev.quran.databinding.ActivityDetailSurahBinding
import com.setiadev.quran.databinding.CustomViewAlertDialogBinding
import com.setiadev.quran.network.Resource
import com.setiadev.quran.network.quran.Ayah
import com.setiadev.quran.network.quran.Surah
import com.setiadev.quran.network.quran.AyahsItem
import com.setiadev.quran.presentation.ViewModelFactory

class DetailSurahActivity : AppCompatActivity() {

    private var _binding: ActivityDetailSurahBinding? = null
    private val binding get() = _binding as ActivityDetailSurahBinding

    private var _surah: Surah? = null
    private val surah get() = _surah as Surah

    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer get() = _mediaPlayer as MediaPlayer

    private val quranViewModel: QuranViewModel by viewModels { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailSurahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _surah = intent.getParcelableExtra(EXTRA_DATA, Surah::class.java)

        initView()

        val mAdapter = SurahAdapter()
        mAdapter.setOnItemClickCallback(object : SurahAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Ayah) {
                showCustomAlertDialog(data, surah)
            }
        })

        val numberSurah = surah.number
        if (numberSurah != null) {
            quranViewModel.getDetailSurahWithQuranEdition(numberSurah).observe(this) {
                when (it) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        binding.rvSurah.apply {
                            mAdapter.setData(it.data?.get(0)?.listAyahs, it.data)
                            adapter = mAdapter
                            layoutManager = LinearLayoutManager(this@DetailSurahActivity)
                        }
                        showLoading(false)
                    }

                    is Resource.Error -> {
                        Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Number Surah not Found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                cvDetailSurah.visibility = View.GONE
                rvSurah.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                cvDetailSurah.visibility = View.VISIBLE
                rvSurah.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {
        binding.apply {
            val revelationType = surah.revelationType
            val numberOfAyahs = surah.numberOfAyahs
            val resultOfAyah = "$revelationType - $numberOfAyahs Ayahs"
            tvDetailAyah.text = resultOfAyah
            tvDetailName.text = surah.name
            tvDetailSurah.text = surah.englishName
            tvDetailNameTranslation.text = surah.englishNameTranslation
        }
    }

    private fun showCustomAlertDialog(dataAudio: Ayah, surah: Surah) {
        _mediaPlayer = MediaPlayer()
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = CustomViewAlertDialogBinding.inflate(layoutInflater)
        builder.setView(view.root)
        view.apply {
            tvSurah.text = surah.englishName
            tvName.text = surah.name
            val numberInSurah = dataAudio.numberInSurah
            val resultNumberText = "Ayah $numberInSurah"
            tvNumberAyah.text = resultNumberText
        }
        view.btnPlay.setOnClickListener {
            it.isEnabled = false
            view.btnPlay.text = getString(R.string.playing_audio)
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                mediaPlayer.setDataSource(dataAudio.audio)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        view.btnCancel.setOnClickListener {
            mediaPlayer.stop()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
        mediaPlayer.setOnCompletionListener {
            builder.dismiss()
        }
    }

    companion object {
        const val EXTRA_DATA = "number"
    }
}