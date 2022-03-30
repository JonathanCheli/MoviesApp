package com.example.smartmobilefactory_app_jc.page.episode

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.arch.ArchBinder
import com.example.smartmobilefactory_app_jc.page.episode.Contract.Command
import com.example.smartmobilefactory_app_jc.page.episode.Contract.Model
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showContent
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showError
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showLoad
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeActivity : AppCompatActivity(), Contract.View {

    @Inject
    override lateinit var subscriptions: CompositeDisposable
    @Inject
    lateinit var presenter: EpisodePresenter
    @Inject
    lateinit var interactor: EpisodeInteractor
    @Inject
    lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = BehaviorSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_episode)
        window.statusBarColor = Color.parseColor("#FF000000")
        window.navigationBarColor = Color.parseColor("#FF000000")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { _, insets ->
            val lp = toolbar.layoutParams as CoordinatorLayout.LayoutParams
            lp.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            toolbar.layoutParams = lp

            val navBarProtection = findViewById<View>(R.id.nav_bar_protection)
            val nlp = navBarProtection.layoutParams as ConstraintLayout.LayoutParams
            nlp.height = insets.systemWindowInsetBottom
            navBarProtection.layoutParams = nlp

            insets.consumeSystemWindowInsets()
        }

        findViewById<CoordinatorLayout>(R.id.container).systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial(intent.extras?.getLong("EPISODE_ID")))
    }

    override fun render(model: Model) {
        val edgeCaseContent = findViewById<View>(R.id.edge_case_content)
        val content = findViewById<View>(R.id.content)

        when (model) {
            is Model.Loading -> showLoad(edgeCaseContent, content)
            is Model.Error -> showError(edgeCaseContent, content, model.message)
            is Model.NoShow -> showError(edgeCaseContent, content, model.message)
            is Model.EpisodeModel -> {
                showContent(edgeCaseContent, content)
                renderShowModel(model)
            }
        }
    }

    private fun renderShowModel(model: Model.EpisodeModel) {
        val banner = findViewById<ImageView>(R.id.banner)
        val name = findViewById<TextView>(R.id.name)
        val summary = findViewById<TextView>(R.id.summary)
        val seasonValue = findViewById<TextView>(R.id.season_value)
        val numberValue = findViewById<TextView>(R.id.number_value)

        name.text = model.episode.name
        summary.text = model.summary
        seasonValue.text = model.episode.season.toString()
        numberValue.text = model.episode.number.toString()

        Glide.with(this)
            .load(model.episode.image ?: "")
            .placeholder(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(banner)
    }

}
