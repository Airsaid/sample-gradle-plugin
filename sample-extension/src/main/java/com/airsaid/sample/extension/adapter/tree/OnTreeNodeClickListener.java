package com.airsaid.sample.extension.adapter.tree;

import android.view.View;

import androidx.annotation.Nullable;

/**
 * The tree node click listener. It's responsible for the tree node click event.
 * So you could receive the event. when you try to do something.
 * But actually it's not for the parent node. Cause we help you close or open the sub-nodes.
 * <p>
 * If you want to receive the click event from parent node. Take a look at {@link TreeAdapter# onNodeExpand(com.cz.widget.recyclerview.adapter.support.tree.TreeNode, java.lang.Object, androidx.recyclerview.widget.RecyclerView.ViewHolder, boolean)}
 *
 * @author JackChen
 * @see TreeAdapter#setOnTreeNodeClickListener(OnTreeNodeClickListener)
 */
public interface OnTreeNodeClickListener<E> {
  void onNodeItemClick(@Nullable TreeNode<E> node, @Nullable E item, @Nullable View v, int position);
}