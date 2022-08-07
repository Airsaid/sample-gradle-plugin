package com.airsaid.sample.core.main.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.AndroidSample
import com.airsaid.sample.core.R
import com.airsaid.sample.core.SampleConstants
import com.airsaid.sample.core.databinding.SampleFragmentMainLayoutBinding
import com.airsaid.sample.core.main.adapter.MutableListAdapter
import com.airsaid.sample.core.main.adapter.SampleListAdapter
import com.airsaid.sample.core.model.PathNode
import com.airsaid.sample.core.util.appName

/**
 * @author JackChen
 */
class DefaultMainSampleFragment : Fragment() {

  private lateinit var binding: SampleFragmentMainLayoutBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = SampleFragmentMainLayoutBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val activity = requireActivity() as AppCompatActivity
    val intent = activity.intent
    val title = intent.getStringExtra(SampleConstants.PARAMETER_TITLE)
    val desc = intent.getStringExtra(SampleConstants.PARAMETER_DESC)
    val path = intent.getStringExtra(SampleConstants.PARAMETER_PATH)
    if (null == path) {
      binding.sampleToolbar.title = requireContext().appName()
      activity.setSupportActionBar(binding.sampleToolbar)
      // Here show all the testCases
      val testCases = AndroidSample.getTestCases()
      if (testCases.isNotEmpty()) {
        alertTestCaseDialog(AndroidSample, activity, testCases)
      }
    } else {
      binding.sampleToolbar.title = title
      binding.sampleToolbar.subtitle = desc
      activity.setSupportActionBar(binding.sampleToolbar)
      activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
      binding.sampleToolbar.setNavigationOnClickListener { activity.finish() }
    }
    val pathNodeList = AndroidSample.getPathNodeList(path)
    val itemDecoration = DividerItemDecoration(
      activity,
      LinearLayoutManager.VERTICAL
    )
    itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.sample_list_divider)!!)
    binding.sampleList.addItemDecoration(itemDecoration)
    binding.sampleList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    val adapter = SampleListAdapter(pathNodeList.toMutableList())
    binding.sampleList.adapter = adapter
    adapter.setOnItemClickListener(object : MutableListAdapter.OnItemClickListener<PathNode> {
      override fun onItemClick(list: List<PathNode>, v: View, position: Int) {
        val pathNode = list[position]
        val item = pathNode.item
        if (item is SampleItem) {
          AndroidSample.start(activity, item)
          return
        }
        val category = item.toString()
        val subDirectory = "${path ?: "."}/$category"
        val activityIntent = getLauncherActivityIntent(activity)
        val activityClass = activityIntent?.component?.className
        if (null != activityClass) {
          val clazz = Class.forName(activityClass)
          Intent(requireActivity(), clazz).apply {
            putExtra(SampleConstants.PARAMETER_TITLE, category.replaceFirstChar { it.uppercaseChar() })
            putExtra(SampleConstants.PARAMETER_PATH, subDirectory)
            startActivity(this)
          }
        }
      }
    })
  }

  /**
   * Show all the testCases.
   *
   * @param context
   * @param testCases
   */
  private fun alertTestCaseDialog(
    androidSample: AndroidSample,
    context: AppCompatActivity,
    testCases: List<SampleItem>
  ) {
    if (1 == testCases.size) {
      // Run this testcase immediately
      androidSample.start(context, testCases.first())
    } else {
      val builder = AlertDialog.Builder(context)
      val items = arrayOfNulls<CharSequence>(testCases.size)
      for (i in testCases.indices) {
        val sampleItem = testCases[i]
        items[i] = sampleItem.title
      }
      builder.setTitle(R.string.sample_choice_test_case)
        .setItems(items) { _, i ->
          val sampleItem = testCases[i]
          androidSample.start(context, sampleItem)
        }.setCancelable(true)
      builder.show()
    }
  }

  /**
   * This function return launcher activity component
   */
  private fun getLauncherActivityIntent(context: Context): Intent? {
    return context.packageManager.getLaunchIntentForPackage(context.packageName)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {}

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return false
  }
}
