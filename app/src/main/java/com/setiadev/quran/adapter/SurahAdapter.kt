package com.setiadev.quran.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.setiadev.quran.databinding.ItemAyahBinding
import com.setiadev.quran.network.quran.Ayah
import com.setiadev.quran.network.quran.AyahsItem
import com.setiadev.quran.network.quran.QuranEdition

class SurahAdapter : RecyclerView.Adapter<SurahAdapter.MyViewHolder>() {
    private val listAyah = ArrayList<Ayah>()
    private val listEdition = ArrayList<QuranEdition>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(dataAyah: List<Ayah>?, dataEdition: List<QuranEdition>?) {
        if (dataAyah == null || dataEdition == null) return
        listAyah.clear()
        listAyah.addAll(dataAyah)
        listEdition.clear()
        listEdition.addAll(dataEdition)
    }

    class MyViewHolder(val binding: ItemAyahBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        ItemAyahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = listAyah.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listAyah = listAyah[position]
        val quranAudio = listEdition[1].listAyahs[position]
        val quranTranslationIndo = listEdition[2].listAyahs[position]

        holder.binding.apply {
            tvItemNumberAyah.text = listAyah.numberInSurah.toString()
            tvItemAyah.text = listAyah.text
            tvItemTranslation.text = quranTranslationIndo?.text
            this.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(quranAudio)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Ayah)
    }
}