package kittens

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.disposable.scope.disposableScope
import com.badoo.reaktive.observable.map
import kittens.datasource.KittenDataSource
import kittens.integration.KittenStoreNetwork
import kittens.integration.KittenStoreParser
import kittens.store.KittenStore.State
import kittens.store.KittenStoreImpl
import kittens.integration.toIntent
import kittens.integration.toModel
import kittens.KittenView.Event

class KittenComponent internal constructor(dataSource: KittenDataSource) {

    constructor() : this(KittenDataSource())

    private val store =
        KittenStoreImpl(
            network = KittenStoreNetwork(dataSource = dataSource),
            parser = KittenStoreParser
        )

    private var view: KittenView? = null
    private var startStopScope: DisposableScope? = null

    fun onViewCreated(view: KittenView) {
        this.view = view
    }

    fun onStart() {
        val view = requireNotNull(view)

        startStopScope = disposableScope {
            store.map(State::toModel).subscribeScoped(onNext = view::render)
            view.events.map(Event::toIntent).subscribeScoped(onNext = store::onNext)
        }
    }

    fun onStop() {
        requireNotNull(startStopScope).dispose()
    }

    fun onViewDestroyed() {
        view = null
    }

    fun onDestroy() {
        store.dispose()
    }
}