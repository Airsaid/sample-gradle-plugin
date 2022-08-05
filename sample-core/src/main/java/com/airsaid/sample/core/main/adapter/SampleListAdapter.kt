package com.airsaid.sample.core.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.databinding.SampleListItemBinding
import com.airsaid.sample.core.model.PathNode

/**
 * @author JackChen
 */
class SampleListAdapter(items: MutableList<PathNode>) :
  MutableListAdapter<PathNode, SampleListAdapter.ViewHolder>(items) {

  override fun compareItem(
    checkContent: Boolean,
    first: PathNode,
    second: PathNode
  ): Boolean {
    return if (checkContent) first == second else first === second
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = SampleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(this[position])
    holder.itemView.setOnClickListener { v -> dispatchItemClickEvent(v, holder.layoutPosition) }
  }

  class ViewHolder(private val binding: SampleListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(node: PathNode) {
      val item = node.item
      if (item is String) {
        binding.sampleTitle.text = item.replaceFirstChar { it.uppercaseChar() }
        binding.sampleDescription.visibility = View.GONE
        binding.sampleImageArrow.visibility = View.VISIBLE
      } else if (item is SampleItem) {
        binding.sampleTitle.text = item.title.replaceFirstChar { it.uppercaseChar() }
        if (item.desc.isNotEmpty()) {
          binding.sampleDescription.text = item.desc
          binding.sampleDescription.visibility = View.VISIBLE
        } else {
          binding.sampleDescription.visibility = View.GONE
        }
        binding.sampleImageArrow.visibility = View.GONE
      }
    }
  }
}
