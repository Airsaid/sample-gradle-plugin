package com.airsaid.sample.core.appcompat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.R
import com.airsaid.sample.core.SampleConstants
import com.airsaid.sample.core.window.AppcompatWindowDelegate
import com.airsaid.sample.core.window.WindowDelegate

/**
 * @author JackChen & Airsaid
 */
open class SampleFragmentContainerActivity : AbstractSampleActivity() {

  companion object {
    private const val SAMPLE_FRAGMENT_TAG = "sample_fragment_tag"

    /**
     * Start activity for a fragment sample.
     *
     * @param context The context object.
     * @param sampleItem The sample item object.
     */
    fun startActivity(context: Context, sampleItem: SampleItem) {
      check(Fragment::class.java.isAssignableFrom(sampleItem.clazz())) {
        "SampleActivity only support case that implemented by Fragment!"
      }
      Intent(context, SampleFragmentContainerActivity::class.java).apply {
        putExtra(SampleConstants.PARAMETER_TITLE, sampleItem.title)
        putExtra(SampleConstants.PARAMETER_DESC, sampleItem.desc)
        putExtra(SampleConstants.PARAMETER_FRAGMENT_CLASS, sampleItem.className)
        context.startActivity(this)
      }
    }
  }

  private val windowDelegate: WindowDelegate by lazy {
    AppcompatWindowDelegate()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.sample_activity_fragment_container)
  }

  override fun setContentView(layoutResID: Int) {
    LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      val view = layoutInflater.inflate(layoutResID, this, false)
      setContentViewInternal(getSampleFragment(), this, view)
    }
  }

  private fun setContentViewInternal(fragment: Fragment, contentView: ViewGroup, view: View) {
    val createView = windowDelegate.onCreateView(this, fragment, contentView, view, null)
    if (!hasToolBar(createView)) {
      val toolBar = Toolbar(ContextThemeWrapper(this, R.style.Sample_AppTheme_AppBarOverlay))
      // set toolbar background color
      withStyledAttributes(attrs = intArrayOf(R.attr.colorPrimary)) {
        val colorPrimary = getColor(0, Color.GRAY)
        toolBar.setBackgroundColor(colorPrimary)
      }

      // set toolbar elevation
      toolBar.elevation = resources.getDimension(R.dimen.sample_toolbar_elevation)

      // This Activity already has an action bar supplied by the window decor.
      // Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar
      // to false in your theme to use a Toolbar instead.
      if (!delegate.hasWindowFeature(Window.FEATURE_NO_TITLE)) {
        delegate.requestWindowFeature(Window.FEATURE_NO_TITLE)
      }

      // initialize all the information
      setSupportActionBar(toolBar)
      supportActionBar?.apply {
        title = intent.getStringExtra(SampleConstants.PARAMETER_TITLE)
        subtitle = intent.getStringExtra(SampleConstants.PARAMETER_DESC)
        setDisplayHomeAsUpEnabled(true)
      }
      toolBar.setNavigationOnClickListener { finish() }
      contentView.addView(toolBar, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    // add children view to content view
    contentView.addView(createView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    super.setContentView(contentView)

    // Then add our fragment. now the view is on fragment.
    // actually fragment manager can't find this view by id. So we postpone this process.
    view.post {
      supportFragmentManager.beginTransaction()
        .add(R.id.sampleActivityFragmentContainer, fragment, SAMPLE_FRAGMENT_TAG)
        .commit()
    }
  }

  private fun getSampleFragment(): Fragment {
    val className = intent.getStringExtra(SampleConstants.PARAMETER_FRAGMENT_CLASS)
    return Class.forName(className!!).newInstance() as Fragment
  }
}
