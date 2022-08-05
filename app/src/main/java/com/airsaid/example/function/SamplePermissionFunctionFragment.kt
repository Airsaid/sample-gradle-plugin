package com.airsaid.example.function

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.permission.SamplePermission
import com.airsaid.sample.extension.permission.addPermissionObserver
import com.airsaid.example.R

@SamplePermission(
  Manifest.permission.CAMERA,
  Manifest.permission.WRITE_EXTERNAL_STORAGE
)
@Register(title = "权限功能(Fragment)", desc = "演示动行时权限动态扩展功能(Fragment)")
class SamplePermissionFunctionFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_function_permission_sample, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    addPermissionObserver { result ->
      if (result.granted) {
        val text = getString(R.string.permission_granted, result.name)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
      } else {
        val text = getString(R.string.permission_denied, result.name)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
      }
    }
  }
}
