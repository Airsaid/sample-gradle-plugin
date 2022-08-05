package com.airsaid.sample.core.appcompat

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.withStyledAttributes
import com.airsaid.sample.core.R
import com.airsaid.sample.core.SampleConstants
import com.airsaid.sample.core.main.component.DefaultMainSampleFragment
import com.airsaid.sample.core.window.AppcompatWindowDelegate
import com.airsaid.sample.core.window.WindowDelegate

/**
 * @author JackChen & Airsaid
 */
open class SampleAppCompatActivity : AbstractSampleActivity() {

  companion object {
    private const val BIND_MAIN_SAMPLE_FRAGMENT_TAG = "android_sample_main_fragment"
  }

  private var windowDelegate: WindowDelegate? = null

  private val isLauncherActivity: Boolean
    get() = launchActivityName() == javaClass.name

  /**
   * This is user's original view. However We may change it. or put this view input a fragment.
   *
   * It will causes some problems:
   * ```
   * override fun onCreate(savedInstanceState: Bundle?) {
   *   super.onCreate(savedInstanceState)
   *   setContentView(R.layout.activity_sample)
   *   // Here we may change this content view. and put content view into fragment.
   *   // Then If you try to findViewById It doesn't existed.
   *   findViewById<Button>(R.id.testButton)
   * }
   * ```
   * So we keep this view. If `findViewById` can't find the view. we try to find view from it.
   */
  private var originalContentView: View? = null

  override fun setContentView(layoutResID: Int) {
    if (isLauncherActivity) {
      injectMainComponent()
    } else {
      LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        val view = layoutInflater.inflate(layoutResID, this, false)
        setContentViewInternal(this, view)
      }
    }
  }

  override fun setContentView(view: View) {
    if (isLauncherActivity) {
      injectMainComponent()
    } else {
      LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setContentViewInternal(this, view)
      }
    }
  }

  override fun <T : View?> findViewById(id: Int): T? {
    return super.findViewById(id) ?: originalContentView?.findViewById<T>(id)
  }

  private fun setContentViewInternal(contentView: ViewGroup, view: View) {
    this.originalContentView = view
    if (windowDelegate == null) {
      windowDelegate = AppcompatWindowDelegate()
    }
    if (hasToolBar(view)) {
      val createView = windowDelegate!!.onCreateView(this, this, contentView, view, null)
      super.setContentView(createView)
    } else {
      val createView = windowDelegate!!.onCreateView(this, this, contentView, view, null)
      if (!hasToolBar(createView)) {
        val toolBar = Toolbar(ContextThemeWrapper(this, R.style.Sample_AppTheme_AppBarOverlay))
        // set toolbar background color
        withStyledAttributes(attrs = intArrayOf(R.attr.colorPrimary)) {
          val colorPrimary = getColor(0, Color.GRAY)
          toolBar.setBackgroundColor(colorPrimary)
        }

        // set toolbar elevation
        toolBar.elevation = resources.getDimension(R.dimen.sample_toolbar_elevation)

        // Caused by:
        // java.lang.IllegalStateException: This Activity already has an action bar supplied by the window decor.
        //  Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme
        //  to use a Toolbar instead.
        //  at androidx.appcompat.app.AppCompatDelegateImpl.setSupportActionBar(AppCompatDelegateImpl.java:421)
        // For this problem. I use this solution:
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
    }
  }

  /**
   * Check this activity if is main activity we will inject our fragment.
   */
  private fun Activity.injectMainComponent() {
    if (!isLauncherActivity) return

    require(this is AppCompatActivity) {
      "The main activity should extends from AppCompatActivity! We can't support the Activity!"
    }

    supportFragmentManager.findFragmentByTag(BIND_MAIN_SAMPLE_FRAGMENT_TAG) ?: run {
      supportFragmentManager.beginTransaction()
        .add(android.R.id.content, DefaultMainSampleFragment(), BIND_MAIN_SAMPLE_FRAGMENT_TAG)
        .commit()
    }
  }

  /**
   * Returns the `android.intent.action.MAIN` activity class name.
   */
  private fun Context.launchActivityName(): String {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    return intent?.component?.className ?: ""
  }
}
