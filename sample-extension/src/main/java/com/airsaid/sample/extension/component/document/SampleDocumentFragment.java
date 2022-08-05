package com.airsaid.sample.extension.component.document;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.airsaid.sample.extension.R;
import com.airsaid.sample.extension.component.document.view.MarkdownView;
import com.airsaid.sample.extension.view.WebViewProgressBar;

/**
 * @author JackChen
 */
public class SampleDocumentFragment extends Fragment {
  private final static String URL = "url";
  private final static String PACKAGE_NAME = "package_name";

  public static Fragment newInstance(String packageName, String url) {
    Bundle argument = new Bundle();
    argument.putString(URL, url);
    argument.putString(PACKAGE_NAME, packageName);
    Fragment fragment = new SampleDocumentFragment();
    fragment.setArguments(argument);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.sample_fragment_document, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    //初始化markdown
    Bundle arguments = getArguments();
    String url = arguments.getString(URL);
    String packageName = arguments.getString(PACKAGE_NAME);
    if (null != url && null != packageName) {
      //设置base_url session
      initLoadProgress();
      initMarkdown(url, packageName);
    }
  }

  /**
   * 初始化markdown显示进度
   */
  private void initLoadProgress() {
    //关联进度显示
    View view = getView();
    MarkdownView markdownView = view.findViewById(R.id.sampleMarkdownView);
    final WebViewProgressBar sampleProgressBar = view.findViewById(R.id.sampleProgressBar);
    sampleProgressBar.startProgressAnim();
    sampleProgressBar.setOnProgressListener(new WebViewProgressBar.OnProgressListener() {
      @Override
      public void onLoadFinished(View v) {
        v.animate().alpha(0f);
      }
    });
    markdownView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress >= sampleProgressBar.getFirstProgress()) {
          sampleProgressBar.passAnimation();
        }
      }
    });
  }

  private void initMarkdown(String url, String packageName) {
    FragmentActivity activity = getActivity();
    MarkdownView markdownView = activity.findViewById(R.id.sampleMarkdownView);
    if (TextUtils.isEmpty(url)) {
      markdownView.loadUrl("about:blank");
    } else {
      if (url.startsWith("http")) {
        markdownView.loadUrl(url);
      } else if (url.startsWith("assets://")) {
        url = url.substring("assets://".length());
        markdownView.loadMarkdownFromAssets(url, null);
      } else {
        DocumentAssetsManager documentAssetsManager = DocumentAssetsManager.getInstance();
        String assetsPath = documentAssetsManager.findDocument(url);
        if (null == assetsPath) {
          markdownView.loadUrl("about:blank");
        } else {
          markdownView.loadMarkdownFromAssets(assetsPath, null);
        }
      }
    }
  }

}
