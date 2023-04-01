package com.example.pusheencatsimulator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pusheencatsimulator.databinding.ItemListBinding

class AdapterBar(
    private var size: Int
): RecyclerView.Adapter<AdapterBar.LoaderHolder>() {
    class LoaderHolder(binding: ItemListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoaderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater,parent,false)

        return LoaderHolder(binding)
    }

    override fun onBindViewHolder(holder: LoaderHolder, position: Int) = Unit

    override fun getItemCount() = size
}

