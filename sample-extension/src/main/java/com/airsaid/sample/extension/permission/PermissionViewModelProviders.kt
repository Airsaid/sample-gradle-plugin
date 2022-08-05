package com.airsaid.sample.extension.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory

fun FragmentActivity.addPermissionObserver(observer: Observer<PermissionResult>) {
  PermissionViewModelProviders.getViewModel(this).addObserver(this, observer)
}

fun Fragment.addPermissionObserver(observer: Observer<PermissionResult>) {
  PermissionViewModelProviders.getViewModel(this).addObserver(this, observer)
}

object PermissionViewModelProviders {
  fun getViewModel(activity: FragmentActivity): PermissionViewModel {
    val newInstanceFactory: ViewModelProvider.Factory = NewInstanceFactory()
    val viewModelProvider = ViewModelProvider(activity, newInstanceFactory)
    return viewModelProvider.get(PermissionViewModel::class.java)
  }

  /**
   * We will only use the activity's viewModelStore to store the ViewModel.
   * If we use the different lifecycleOwner with different ViewModelStore.
   * The fragment will never receive the event.
   *
   * @param fragment
   * @return
   */
  fun getViewModel(fragment: Fragment): PermissionViewModel {
    val activity = fragment.activity
    val newInstanceFactory: ViewModelProvider.Factory = NewInstanceFactory()
    val viewModelProvider = ViewModelProvider(activity!!.viewModelStore, newInstanceFactory)
    return viewModelProvider.get(PermissionViewModel::class.java)
  }
}
