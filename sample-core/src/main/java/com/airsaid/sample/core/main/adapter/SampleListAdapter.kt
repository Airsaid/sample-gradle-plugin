package com.airsaid.sample.core.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.databinding.SampleListItemBinding
import com.airsaid.sample.core.model.PathNode
import com.airsaid.sample.core.util.displayDesc
import com.airsaid.sample.core.util.displayTitle

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
      binding.sampleTitle.text = node.displayTitle()
      val desc = node.displayDesc()
      if (desc.isNotEmpty()) {
        binding.sampleDescription.text = desc
        binding.sampleDescription.visibility = View.VISIBLE
      } else {
        binding.sampleDescription.visibility = View.GONE
      }

      val item = node.item
      if (item is String) {
        binding.sampleImageArrow.visibility = View.VISIBLE
      } else if (item is SampleItem) {
        binding.sampleImageArrow.visibility = View.GONE
      }
    }
  }
}
