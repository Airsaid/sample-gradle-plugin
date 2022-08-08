package com.airsaid.sample.extension.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airsaid.sample.api.Extension
import com.airsaid.sample.core.appcompat.SampleWrapperViewFragment
import com.airsaid.sample.core.component.ComponentContainer
import com.airsaid.sample.extension.R
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.sample.extension.component.code.SampleSourceFileFragmentListFragment
import com.airsaid.sample.extension.component.document.SampleDocument
import com.airsaid.sample.extension.component.document.SampleDocumentFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Extension
open class SamplePagerComponentContainer : ComponentContainer {

  override fun isComponentAvailable(component: Any): Boolean {
    val sampleSourceCode = component.javaClass.getAnnotation(SampleSourceCode::class.java)
    val sampleDocument = component.javaClass.getAnnotation(SampleDocument::class.java)
    return sampleSourceCode?.regex != null || sampleDocument?.value != null
  }

  override fun getComponentView(
    context: AppCompatActivity,
    component: Any,
    parentView: ViewGroup,
    view: View
  ): View {
    val layoutInflater = LayoutInflater.from(context)
    val contentLayout = layoutInflater.inflate(R.layout.sample_fragment_tab, parentView, false)
    val sampleTabLayout = contentLayout.findViewById<TabLayout>(R.id.sampleTabLayout)
    val sampleViewPager = contentLayout.findViewById<ViewPager2>(R.id.sampleViewPager)
    val titleList: MutableList<CharSequence> = ArrayList()
    titleList.add(context.getString(R.string.sample))
    val fragmentList: MutableList<Fragment> = java.util.ArrayList()
    fragmentList.add(SampleWrapperViewFragment.newFragment(view))

    val sampleSourceCode = component.javaClass.getAnnotation(SampleSourceCode::class.java)
    if (null != sampleSourceCode) {
      val filter = sampleSourceCode.regex
      // Plus our component
      titleList.add(context.getString(R.string.sample_source_code))
      val packageName = component.javaClass.getPackage()!!.name
      fragmentList.add(SampleSourceFileFragmentListFragment.newInstance(packageName, filter))
    }

    val sampleDocument = component.javaClass.getAnnotation(SampleDocument::class.java)
    if (null != sampleDocument) {
      val url = sampleDocument.value
      val packageName = component.javaClass.getPackage()!!.name
      titleList.add(context.getString(R.string.sample_document))
      fragmentList.add(SampleDocumentFragment.newInstance(packageName, url))
    }
    sampleViewPager.offscreenPageLimit = 3
    sampleViewPager.adapter = object : FragmentStateAdapter(context) {
      override fun getItemCount(): Int {
        return fragmentList.size
      }

      override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
      }
    }
    TabLayoutMediator(sampleTabLayout, sampleViewPager) { tab, position ->
      tab.text = titleList[position]
    }.attach()
    return contentLayout
  }

  override fun onCreatedView(context: AppCompatActivity, instance: Any, componentView: View) = Unit

  override fun getComponentPriority(): Int = 999
}
