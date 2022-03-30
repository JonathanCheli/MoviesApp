package com.example.smartmobilefactory_app_jc.widget

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import com.example.smartmobilefactory_app_jc.R

object EdgeCaseContent {

    fun showLoad(edgeCaseContent: View, content: View) {
        val error = edgeCaseContent.findViewById<TextView>(R.id.error)
        val load = edgeCaseContent.findViewById<ProgressBar>(R.id.load)
        error.visibility = GONE
        load.visibility = VISIBLE
        edgeCaseContent.visibility = VISIBLE
        content.visibility = GONE
    }

    fun showError(edgeCaseContent: View, content: View, errorMessage: String) {
        val error = edgeCaseContent.findViewById<TextView>(R.id.error)
        val load = edgeCaseContent.findViewById<ProgressBar>(R.id.load)
        error.visibility = VISIBLE
        load.visibility = GONE
        edgeCaseContent.visibility = VISIBLE
        content.visibility = GONE
        error.text = errorMessage
    }

    fun showContent(edgeCaseContent: View, content: View) {
        val error = edgeCaseContent.findViewById<TextView>(R.id.error)
        val load = edgeCaseContent.findViewById<ProgressBar>(R.id.load)
        error.visibility = GONE
        load.visibility = GONE
        edgeCaseContent.visibility = GONE
        content.visibility = VISIBLE
    }

}
