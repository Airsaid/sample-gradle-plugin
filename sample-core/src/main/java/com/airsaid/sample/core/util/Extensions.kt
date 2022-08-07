package com.airsaid.sample.core.util

import android.content.Context

fun Context.appName() =
  packageManager?.getPackageInfo(packageName, 0)?.applicationInfo?.loadLabel(packageManager)?.toString() ?: ""
