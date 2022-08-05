package com.airsaid.sample.extension.permission

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class SamplePermissionsFragment : Fragment() {
  private var pendingRequestRunnable: Runnable? = null

  companion object {
    private const val PERMISSIONS_REQUEST_CODE = 1024
    private const val BIND_SAMPLE_PERMISSION_FRAGMENT_TAG = "sample.permission.bind_fragment_tag"
    fun injectIfNeededIn(activity: FragmentActivity) {
      val supportFragmentManager = activity.supportFragmentManager
      if (supportFragmentManager.findFragmentByTag(BIND_SAMPLE_PERMISSION_FRAGMENT_TAG) == null) {
        supportFragmentManager.beginTransaction()
          .add(SamplePermissionsFragment(), BIND_SAMPLE_PERMISSION_FRAGMENT_TAG).commitNow()
      }
    }

    fun get(activity: FragmentActivity): SamplePermissionsFragment {
      val supportFragmentManager = activity.supportFragmentManager
      val fragment = supportFragmentManager.findFragmentByTag(BIND_SAMPLE_PERMISSION_FRAGMENT_TAG)
      return fragment as SamplePermissionsFragment
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    pendingRequestRunnable?.run()
  }

  fun requestPermissions(permissions: Array<out String>) {
    if (isAdded) {
      requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
    } else {
      pendingRequestRunnable = Runnable { requestPermissions(permissions, PERMISSIONS_REQUEST_CODE) }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode != PERMISSIONS_REQUEST_CODE) return
    val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
    for (i in permissions.indices) {
      shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
    }
    onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
  }

  private fun onRequestPermissionsResult(
    permissions: Array<String>,
    grantResults: IntArray,
    shouldShowRequestPermissionRationale: BooleanArray
  ) {
    var i = 0
    val viewModel = PermissionViewModelProviders.getViewModel(requireActivity())
    while (i < permissions.size) {
      val isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED
      val permissionResult = PermissionResult(
        permissions[i], isGranted, shouldShowRequestPermissionRationale[i]
      )
      viewModel.setPermissionResult(permissionResult)
      i++
    }
  }
}
