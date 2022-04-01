package org.d3if0084.jadwalhariini.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if0084.jadwalhariini.databinding.ItemMainBinding

class MainAdapter(private val handler: ClickHandler) : ListAdapter<Catatan, MainAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Catatan>() {
            override fun areItemsTheSame(
                oldData: Catatan, newData: Catatan
            ): Boolean {
                return oldData.id == newData.id
            }
            override fun areContentsTheSame(
                oldData: Catatan, newData: Catatan
            ): Boolean {
                return oldData == newData
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class ViewHolder(
        private val binding: ItemMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(catatan: Catatan) {
            binding.nimTextView.text = catatan.catatan
            binding.namaTextView.text = catatan.jam
            val pos = absoluteAdapterPosition
            itemView.isSelected = selectionIds.contains(catatan.id)
            itemView.setOnClickListener { handler.onClick(pos, catatan) }
            itemView.setOnLongClickListener { handler.onLongClick(pos) }
        }
    }
    interface ClickHandler {
        fun onClick(position: Int, catatan: Catatan)
        fun onLongClick(position: Int): Boolean

    }
    private val selectionIds = ArrayList<Int>()
    fun toggleSelection(pos: Int) {
        val id = getItem(pos).id
        if (selectionIds.contains(id))
            selectionIds.remove(id)
        else
            selectionIds.add(id)
        notifyDataSetChanged()
    }
    fun getSelection(): List<Int> {
        return selectionIds
    }
    fun resetSelection() {
        selectionIds.clear()
        notifyDataSetChanged()
    }
}