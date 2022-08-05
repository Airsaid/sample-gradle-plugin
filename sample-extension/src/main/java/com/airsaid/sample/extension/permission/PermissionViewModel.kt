package com.airsaid.sample.extension.permission

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

/**
 * @author JackChen
 */
class PermissionViewModel : ViewModel() {
  private val permissionLiveData = MutableLiveData<PermissionResult>()

  fun setPermissionResult(permissionResult: PermissionResult) {
    permissionLiveData.value = permissionResult
  }

  @MainThread
  fun addObserver(lifecycleOwner: LifecycleOwner, observer: Observer<PermissionResult>) {
    permissionLiveData.observe(lifecycleOwner, observer)
  }
}
