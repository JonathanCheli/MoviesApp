package com.example.smartmobilefactory_app_jc.page.seasons


import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.arch.ArchBinder
import com.example.smartmobilefactory_app_jc.page.seasons.Contract.Command
import com.example.smartmobilefactory_app_jc.page.seasons.Contract.Model
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showContent
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showError
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showLoad
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class SeasonsActivity : AppCompatActivity(), Contract.View {

    @Inject
    override lateinit var subscriptions: CompositeDisposable
    @Inject
    lateinit var presenter: SeasonsPresenter
    @Inject
    lateinit var interactor: SeasonsInteractor
    @Inject
    lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = BehaviorSubject.create()

    private val seasonAdapter = SeasonAdapter(object : SeasonsEpisodeCallback {
        override fun onEpisodeClick(id: Long) {
            output.onNext(Command.EpisodeClick(id))
            output.onNext(Command.None)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_seasons)
        window.statusBarColor = Color.parseColor("#FF000000")
        window.navigationBarColor = Color.parseColor("#FF000000")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = seasonAdapter

        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial(intent.extras?.getLong("SHOW_ID")))
    }

    override fun render(model: Model) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val edgeCaseContent = findViewById<View>(R.id.edge_case_content)
        val list = findViewById<RecyclerView>(R.id.list)

        when (model) {
            is Model.Loading -> showLoad(edgeCaseContent, list)
            is Model.Error -> showError(edgeCaseContent, list, model.message)
            is Model.DataModel -> {
                showContent(edgeCaseContent, list)
                toolbar.title = model.title
                seasonAdapter.setItems(model.items)
            }
        }
    }

}
