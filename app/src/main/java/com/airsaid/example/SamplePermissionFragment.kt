package com.airsaid.example

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.permission.SamplePermission
import com.airsaid.sample.extension.permission.addPermissionObserver

@SamplePermission(
  Manifest.permission.CAMERA,
  Manifest.permission.READ_EXTERNAL_STORAGE
)
@Register(
  title = "SamplePermission",
  desc = "Use @SamplePermission and specify the permissions to be requested to " +
    "automatically request permissions when the page is first opened."
)
class SamplePermissionFragment : Fragment() {

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    addPermissionObserver { result ->
      if (result.granted) {
        Toast.makeText(context, "${result.name} Permission request successful!", Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(context, "${result.name} Permission request failed!", Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_sample_text, container, false)
  }

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<TextView>(R.id.textView).apply {
      text = "The addPermissionObserver() allows you to see if the permission request is successful."
    }
  }
}
