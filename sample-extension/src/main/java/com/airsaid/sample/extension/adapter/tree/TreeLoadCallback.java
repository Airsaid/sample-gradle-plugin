package com.airsaid.sample.extension.adapter.tree;


import androidx.annotation.NonNull;

import java.util.List;

/**
 * This callback if for the adapter to load tree node lazily.
 * If you have huge data to load to the tree. You could just load it lazily like traversal the file system.
 *
 * @author JackChen
 * @see TreeAdapter#setLoadCallback(TreeLoadCallback)
 */
public interface TreeLoadCallback<E> {
  /**
   * Return node list from the giving tree node.
   */
  @NonNull
  List<TreeNode<E>> onLoad(TreeNode<E> node);
}