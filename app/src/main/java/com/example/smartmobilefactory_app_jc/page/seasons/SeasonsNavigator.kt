package com.example.smartmobilefactory_app_jc.page.seasons

import android.app.Activity
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class SeasonsNavigator @Inject constructor(
    @ActivityContext context: Context,
) {

    private val activityRef = WeakReference(context as Activity)

    fun openSeason(showId: Long) {
        activityRef.get()?.let { activity ->
            val intent = Intent(activity, SeasonsActivity::class.java)
            intent.putExtra("SHOW_ID", showId)
            activity.startActivity(intent)
        }
    }

}
