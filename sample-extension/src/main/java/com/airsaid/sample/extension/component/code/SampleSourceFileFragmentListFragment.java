package com.airsaid.sample.extension.component.code;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airsaid.sample.extension.R;
import com.airsaid.sample.extension.adapter.tree.TreeNode;
import com.airsaid.sample.extension.component.code.adapter.SampleSourceCodeAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author JackChen
 */
public class SampleSourceFileFragmentListFragment extends Fragment {
  private final static String SAMPLE_PACKAGE_NAME = "samplePackage";
  private final static String SAMPLE_FILE_FILTER_NAME = "sampleFileFilter";
  private final static String SAMPLE_SOURCE_DIALOG = "sample_source_dialog";

  private final SparseArray<BottomSheetDialogFragment> cachedDialogFragments = new SparseArray<>();

  public static Fragment newInstance(String samplePackageName, String filter) {
    Bundle argument = new Bundle();
    argument.putString(SAMPLE_PACKAGE_NAME, samplePackageName);
    argument.putString(SAMPLE_FILE_FILTER_NAME, filter);
    Fragment fragment = new SampleSourceFileFragmentListFragment();
    fragment.setArguments(argument);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.sample_fragment_source_code_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Bundle arguments = getArguments();
    String packageName = Objects.requireNonNull(arguments).getString(SAMPLE_PACKAGE_NAME);
    String filterPattern = Objects.requireNonNull(arguments).getString(SAMPLE_FILE_FILTER_NAME);
    RecyclerView sampleSourceCodeList = view.findViewById(R.id.sampleSourceCodeList);

    TreeNode<String> rootNode = new TreeNode<>(null);
    AssetManager assets = requireContext().getAssets();
    buildFileTree(assets, rootNode, packageName.replace('.', '/'), filterPattern);
    SampleSourceCodeAdapter sampleSourceCodeAdapter = new SampleSourceCodeAdapter(requireContext(), rootNode);

    sampleSourceCodeList.setLayoutManager(new LinearLayoutManager(requireContext()));
    sampleSourceCodeList.addItemDecoration(new SampleFileItemDecoration(requireContext()));
    sampleSourceCodeList.setAdapter(sampleSourceCodeAdapter);
    sampleSourceCodeAdapter.expandAll();
    sampleSourceCodeAdapter.setOnTreeNodeClickListener((node, item, v, i) -> {
      String filePath = sampleSourceCodeAdapter.getItem(i);
      FragmentActivity activity = requireActivity();
      FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
      BottomSheetDialogFragment bottomSheetDialogFragment = cachedDialogFragments.get(i);
      if (null != bottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(supportFragmentManager, SAMPLE_SOURCE_DIALOG);
      } else {
        BottomSheetDialogFragment dialogFragment = SampleSourceCodeDialogFragment.newInstance(filePath);
        cachedDialogFragments.put(i, dialogFragment);
        dialogFragment.show(supportFragmentManager, SAMPLE_SOURCE_DIALOG);
      }
    });
  }

  private void buildFileTree(AssetManager assets, TreeNode<String> parentNode,
                             String path, String filterPattern) {
    try {
      String[] fileList = assets.list(path);
      if (fileList == null || fileList.length <= 0) return;

      for (String filePath : fileList) {
        String classFilePath = path + "/" + filePath;
        if (TextUtils.isEmpty(filterPattern)) {
          TreeNode<String> childNode = new TreeNode<>(parentNode, classFilePath);
          parentNode.children.add(childNode);
          buildFileTree(assets, childNode, classFilePath, null);
        } else {
          boolean isMatch = Pattern.matches(filterPattern, filePath);
          if (isMatch) {
            TreeNode<String> childNode = new TreeNode<>(parentNode, classFilePath);
            parentNode.children.add(childNode);
            buildFileTree(assets, childNode, classFilePath, filterPattern);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDetach() {
    cachedDialogFragments.clear();
    super.onDetach();
  }
}
