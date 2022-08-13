package com.airsaid.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.document.SampleDocument

@SampleDocument("https://raw.githubusercontent.com/Airsaid/sample-gradle-plugin/master/README.md")
@Register(
  title = "SampleDocument",
  desc = "Use @SampleDocument to associate documents to sample."
)
class SampleDocumentFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_sample_text, container, false)
  }

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<TextView>(R.id.textView).apply {
      text = "Click the ${requireContext().resources.getString(R.string.sample_document)} " +
        "table to see the associated document."
    }
  }
}
