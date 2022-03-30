package com.example.smartmobilefactory_app_jc.page.series


import com.example.smartmobilefactory_app_jc.page.series.Contract.Command
import com.example.smartmobilefactory_app_jc.page.series.Contract.Model
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.arch.ArchBinder
import com.example.smartmobilefactory_app_jc.tile.SeriesTileCallback
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showContent
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showError
import com.example.smartmobilefactory_app_jc.widget.EdgeCaseContent.showLoad
import com.example.smartmobilefactory_app_jc.widget.Reselectable
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class SeriesFragment : Fragment(), Reselectable, Contract.View {

    @Inject
    override lateinit var subscriptions: CompositeDisposable
    @Inject
    lateinit var presenter: SeriesPresenter
    @Inject
    lateinit var interactor: SeriesInteractor
    @Inject
    lateinit var archBinder: ArchBinder

    private val adapter = SeriesAdapter({
        output.onNext(Command.NextPage)
    }, object : SeriesTileCallback {
        override fun onItemClick(id: Long) {
            output.onNext(Command.SeriesSelected(id))
        }
    })

    override val output: Subject<Command> = BehaviorSubject.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        list.adapter = adapter
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_series_go_to_end -> list.scrollToPosition(max(adapter.itemCount - 1, 0))
                R.id.action_series_sort -> output.onNext(Command.ChangeSort)
                else -> null
            } != null
        }
        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial)
    }

    override fun onReselected() {
        view?.findViewById<RecyclerView>(R.id.list)?.scrollToPosition(0)
    }

    override fun render(model: Model) {
        val view = view ?: return
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val edgeCaseContent = view.findViewById<View>(R.id.edge_case_content)
        val content = view.findViewById<RecyclerView>(R.id.list)


        when (model) {
            is Model.LoadModel -> showLoad(edgeCaseContent, content)
            is Model.Error -> {
                showError(edgeCaseContent, content, model.message)
                toolbar.title = model.title
            }
            is Model.DataModel -> {
                showContent(edgeCaseContent, content)
                adapter.setItems(model.series, model.showNextLoad)
                toolbar.title = model.title
            }
        }
    }

}
