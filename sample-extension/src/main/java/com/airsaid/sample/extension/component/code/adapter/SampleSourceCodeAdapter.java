package com.airsaid.sample.extension.component.code.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airsaid.sample.extension.R;
import com.airsaid.sample.extension.adapter.tree.TreeAdapter;
import com.airsaid.sample.extension.adapter.tree.TreeNode;

/**
 * @author JackChen
 */
public class SampleSourceCodeAdapter extends TreeAdapter<String> {
  private static final int TYPE_FOLDER = 0x00;
  private static final int TYPE_FILE = 0x01;
  private final LayoutInflater layoutInflater;
  private final int padding;

  public SampleSourceCodeAdapter(Context context, TreeNode<String> rootNode) {
    super(rootNode);
    Resources resources = context.getResources();
    this.layoutInflater = LayoutInflater.from(context);
    this.padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.getDisplayMetrics());
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (TYPE_FOLDER == viewType) {
      return new RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.sample_list_folder_item, parent, false)) {};
    } else {
      return new RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.sample_list_file_item, parent, false)) {};
    }
  }

  @Override
  public int getItemViewType(int position) {
    TreeNode<String> node = getNode(position);
    return !node.children.isEmpty() ? TYPE_FOLDER : TYPE_FILE;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, TreeNode<String> node, String filePath, int viewType, int position) {
    holder.itemView.setPadding(padding * node.depth,
        holder.itemView.getPaddingTop(),
        holder.itemView.getPaddingRight(),
        holder.itemView.getPaddingBottom());
    if (TYPE_FOLDER == viewType) {
      TextView text1 = holder.itemView.findViewById(R.id.fileName);
      int i = filePath.lastIndexOf("/");
      String fileName = filePath.substring(i + 1);
      text1.setText(fileName);
    } else if (TYPE_FILE == viewType) {
      TextView text1 = holder.itemView.findViewById(android.R.id.text1);
      int i = filePath.lastIndexOf("/");
      String fileName = filePath.substring(i + 1);
      text1.setText(fileName);
    }
  }

  @Override
  protected void onNodeExpand(TreeNode<String> node, String item, RecyclerView.ViewHolder holder, boolean expand) {
    super.onNodeExpand(node, item, holder, expand);
    View imageFlagView = holder.itemView.findViewById(R.id.imageFlagView);
    imageFlagView.setSelected(expand);
  }
}
