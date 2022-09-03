package com.airsaid.sample.extension.component.code;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airsaid.sample.extension.R;
import com.airsaid.sample.extension.component.code.view.SourceCodeView;
import com.airsaid.sample.extension.view.WebViewProgressBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * @author JackChen
 */
public class SampleSourceCodeDialogFragment extends BottomSheetDialogFragment {
  private final static String SAMPLE_FILE_PATH = "filePath";

  public static BottomSheetDialogFragment newInstance(String filePath) {
    Bundle argument = new Bundle();
    argument.putString(SAMPLE_FILE_PATH, filePath);
    BottomSheetDialogFragment fragment = new SampleSourceCodeDialogFragment();
    fragment.setArguments(argument);
    return fragment;
  }

  private SampleSourceCodeDialogFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View contentView = inflater.inflate(R.layout.sample_fragment_source_code, container, false);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT);
    layoutParams.height = Resources.getSystem().getDisplayMetrics().heightPixels;
    contentView.setLayoutParams(layoutParams);
    return contentView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final SourceCodeView sampleSourceCodeView = view.findViewById(R.id.sampleSourceCodeView);
    final WebViewProgressBar sampleProgressBar = view.findViewById(R.id.sampleProgressBar);
    sampleProgressBar.startProgressAnim();
    sampleProgressBar.setOnProgressListener(v -> v.animate().alpha(0f));
    sampleSourceCodeView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress >= sampleProgressBar.getFirstProgress()) {
          sampleProgressBar.passAnimation();
        }
      }
    });

    String filePath = requireArguments().getString(SAMPLE_FILE_PATH);
    sampleSourceCodeView.loadSourceCodeFromUrl(filePath);
  }

}
