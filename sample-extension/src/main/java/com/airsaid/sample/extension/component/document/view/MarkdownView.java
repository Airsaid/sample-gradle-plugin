package com.airsaid.sample.extension.component.document.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

import androidx.annotation.WorkerThread;

import com.airsaid.sample.extension.utils.IOUtils;
import com.airsaid.sample.extension.utils.UrlConnections;
import com.airsaid.sample.extension.view.NestedWebView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A markdown view extend from webView.
 * Check resources: asset/markdown.
 *
 * @author JackChen
 */
public class MarkdownView extends NestedWebView {
  public static final String BASE_URL = "file:///android_asset/";
  private static final Executor threadExecutor = Executors.newSingleThreadExecutor();

  public MarkdownView(Context context) {
    this(context, null, 0);
  }

  public MarkdownView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MarkdownView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    //this markdown library needed javascript
    setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
    setHorizontalScrollBarEnabled(false);
    setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        int i = consoleMessage.lineNumber();
        String message = consoleMessage.message();
        System.out.println("line:" + i + " message:" + message);
        return super.onConsoleMessage(consoleMessage);
      }

    });
    WebSettings settings = getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
  }

  @Override
  public void loadUrl(String url) {
    if (url.endsWith(".md")) {
      loadMarkdownFromUrl(url);
    } else {
      super.loadUrl(url);
    }
  }

  /**
   * Load Markdown from url to the view as rich formatted HTML. The
   * HTML output will be styled based on the given CSS file.
   *
   * @param url - input in markdown format
   */
  public void loadMarkdownFromUrl(final String url) {
    final Context context = getContext().getApplicationContext();
    threadExecutor.execute(new Runnable() {
      @Override
      public void run() {
        final String source = UrlConnections.getSourceCode(context, url);
        loadMarkdown(context, source, null);
      }
    });
  }

  /**
   * Loads the given Markdown text to the view as rich formatted HTML. The
   * HTML output will be styled based on the given CSS file.
   *
   * @param text - input in markdown format
   * @param cssUrl - a URL to css File. If the file located in the project assets
   * folder then the URL should start with "file:///android_asset/"
   */
  public void loadMarkdownFromText(final String text, final String cssUrl) {
    final Context context = getContext().getApplicationContext();
    threadExecutor.execute(new Runnable() {
      @Override
      public void run() {
        loadMarkdown(context, text, cssUrl);
      }
    });
  }

  /**
   * Loads the given Markdown file to the view
   *
   * @param file - a local store file, It should be a markdown file
   * @param cssUrl - a path to css file. If the file located in the project assets folder
   * then the URL should start with "file:///android_asset/"
   */
  public void loadMarkdownFromFile(final File file, final String cssUrl) {
    // Read file and load markdown
    final Context context = getContext().getApplicationContext();
    threadExecutor.execute(new Runnable() {
      @Override
      public void run() {
        try {
          String text = IOUtils.toString(file.toURI(), Charset.defaultCharset());
          // load markdown by text
          loadMarkdown(context, text, cssUrl);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Loads the given Markdown file to the view
   *
   * @param filePath - a local store file, It should be a markdown file
   * @param cssUrl - a path to css file. If the file located in the project assets folder
   * then the URL should start with "file:///android_asset/"
   */
  public void loadMarkdownFromAssets(final String filePath, final String cssUrl) {
    // Read file and load markdown
    final Context context = getContext().getApplicationContext();
    threadExecutor.execute(() -> {
      AssetManager assetManager = context.getApplicationContext().getAssets();
      try {
        InputStream inputStream = assetManager.open(filePath);
        String text = IOUtils.toString(inputStream, Charset.defaultCharset());
        // load markdown by text
        loadMarkdown(context, text, cssUrl);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Load markdown by text,Here we well use marked js convert text to html source
   *
   * @link https://github.com/markedjs/marked
   */
  @WorkerThread
  private void loadMarkdown(Context context, String text, String cssUrl) {
    if (TextUtils.isEmpty(text)) {
      post(new Runnable() {
        @Override
        public void run() {
          loadDataWithBaseURL(BASE_URL, "about:blank", "text/html", "UTF-8", null);
        }
      });
    } else {
      // load markdown by text
      try {
        AssetManager assets = context.getAssets();
        InputStream inputStream = assets.open("markdown/markdown.html");
        String templateSource = IOUtils.toString(inputStream, Charset.defaultCharset());
        final String html = String.format(templateSource, text);
        post(new Runnable() {
          @Override
          public void run() {
            loadDataWithBaseURL(BASE_URL, html, "text/html", "UTF-8", null);
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
