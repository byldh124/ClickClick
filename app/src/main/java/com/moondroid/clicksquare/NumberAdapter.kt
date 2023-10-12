package com.moondroid.clicksquare

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.moondroid.clicksquare.databinding.ItemNumberBinding
import kotlin.properties.Delegates

@SuppressLint("NotifyDataSetChanged")
class NumberAdapter(private val onClick: (Int) -> Unit) : Adapter<NumberAdapter.ViewHolder>() {
    private var list: List<Int> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Int>) {
        list = newList
    }

    inner class ViewHolder(
        private val binding: ItemNumberBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val metrics = binding.root.context.resources.displayMetrics
            val width = metrics.widthPixels
            binding.tvNumber.width = width / 7
            binding.tvNumber.height = width / 7
            binding.tvNumber.text = list[position].toString()
            binding.tvNumber.setOnClickListener {
                onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.visible(true)
        holder.bind(position)
    }
}