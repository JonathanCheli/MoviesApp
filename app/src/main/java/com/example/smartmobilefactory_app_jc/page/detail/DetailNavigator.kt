package com.example.smartmobilefactory_app_jc.page.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class DetailNavigator @Inject constructor(
    @ActivityContext context: Context,
) {

    private val activityRef = WeakReference(context as Activity)

    fun openDetail(id: Long) {
        activityRef.get()?.let { activity ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("SHOW_ID", id)
            activity.startActivity(intent)
        }
    }

}