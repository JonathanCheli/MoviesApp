package com.example.smartmobilefactory_app_jc.page.episode

import android.app.Activity
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class EpisodeNavigator @Inject constructor(
    @ActivityContext context: Context,
) {

    private val activityRef = WeakReference(context as Activity)

    fun openEpisode(id: Long) {
        activityRef.get()?.let { activity ->
            val intent = Intent(activity, EpisodeActivity::class.java)
            intent.putExtra("EPISODE_ID", id)
            activity.startActivity(intent)
        }
    }

}