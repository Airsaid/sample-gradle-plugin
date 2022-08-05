package com.airsaid.sample.extension.component.code.view

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.WebSettings
import com.airsaid.sample.extension.component.document.DocumentAssetsManager
import com.airsaid.sample.extension.component.document.view.MarkdownView
import com.airsaid.sample.extension.utils.IOUtils
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author Created by cz
 * @date 2020-01-27 22:07
 * @email bingo110@126.com
 * A web view that support display source code
 * Check resources: asset/highlight
 */
class SourceCodeView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  MarkdownView(context, attrs, defStyleAttr) {

  companion object {
    const val BASE_URL = "file:///android_asset/"
  }

  init {
    overScrollMode = OVER_SCROLL_IF_CONTENT_SCROLLS
    val settings = settings
    settings.javaScriptEnabled = true
    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
  }

  /**
   * Load Markdown from url to the view as rich formatted HTML. The
   * HTML output will be styled based on the given CSS file.
   *
   * @param url - input in markdown format
   */
  fun loadSourceCodeFromUrl(url: String) {
    try {
      val context = context
      val assets = context.assets
      val inputStream = assets.open(url)
      val source = IOUtils.toString(inputStream, "utf-8")
      loadSourceCode(context, source, url)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  /**
   * Load source code by text,Here we will use highlight.js to display source code
   * see assets/highlight
   */
  fun loadSourceCode(context: Context, text: String?, url: String) {
    if (TextUtils.isEmpty(text)) {
      post {
        loadDataWithBaseURL(BASE_URL, "about:blank", "text/html", "UTF-8", null)
        invalidate()
      }
    } else if (url.endsWith("md")) {
      val documentAssetsManager = DocumentAssetsManager.getInstance()
      val assetsPath = documentAssetsManager.findDocument(url)
      if (null == assetsPath) {
        loadUrl("about:blank")
      } else {
        loadMarkdownFromAssets(assetsPath, null)
      }
    } else {
      // load source code by text
      try {
        val assets = context.assets
        val inputStream = assets.open("highlight/highlight_template.html")
        val templateSource = IOUtils.toString(inputStream, Charset.defaultCharset())
        val i = url.lastIndexOf(".")
        val language = url.substring(i + 1)
        val html = String.format(templateSource, language, Html.escapeHtml(text))
        post { loadDataWithBaseURL(BASE_URL, html, "text/html", "UTF-8", null) }
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  fun loadSourceCodeFromAssets(context: Context, assetsFilePath: String) {
    try {
      val assets = context.assets
      val text = assets.open(assetsFilePath).bufferedReader().readText()
      val inputStream = assets.open("highlight/highlight_template.html")
      val templateSource = IOUtils.toString(inputStream, Charset.defaultCharset())
      val html = String.format(templateSource, "en", Html.escapeHtml(text))
      post { loadDataWithBaseURL(BASE_URL, html, "text/html", "UTF-8", null) }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
}
