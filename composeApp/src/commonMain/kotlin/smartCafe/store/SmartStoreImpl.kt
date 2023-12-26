//package smartCafe.store
//
//import com.badoo.reaktive.disposable.scope.DisposableScope
//import com.badoo.reaktive.maybe.Maybe
//import com.badoo.reaktive.maybe.asObservable
//import com.badoo.reaktive.maybe.flatMap
//import com.badoo.reaktive.maybe.map
//import com.badoo.reaktive.maybe.observeOn
//import com.badoo.reaktive.observable.Observable
//import com.badoo.reaktive.observable.ObservableObserver
//import com.badoo.reaktive.observable.defaultIfEmpty
//import com.badoo.reaktive.observable.startWithValue
//import com.badoo.reaktive.scheduler.mainScheduler
//import smartCafe.store.SmartStore.Intent
//import smartCafe.store.SmartStore.State
//import mvi.StoreHelper
//
//internal class SmartStoreImpl(
//    private val network:Network,
//    private val parser:Parser
//): SmartStore, DisposableScope by DisposableScope() {
//
//    private val helper = StoreHelper(State(), ::handleIntent, ::reduce).scope()
//    override val state: State get() = helper.state
//
//    init {
//        helper.onIntent(Intent.Reload)
//    }
//
//    override fun onNext(value: Intent) {
//        helper.onIntent(value)
//    }
//
//    override fun subscribe(observer: ObservableObserver<State>) {
//        helper.subscribe(observer)
//    }
//
//    private fun handleIntent(state: State, intent: Intent): Observable<Effect> =
//        when (intent) {
//            is Intent.Reload -> reload(network, parser)
//        }
//
//    private fun reload(network: Network, parser: Parser): Observable<Effect> =
//        network
//            .load()
//            .flatMap(parser::parse)
//            .map(Effect::LoadingFinished)
//            .observeOn(mainScheduler)
//            .asObservable()
//            .defaultIfEmpty(Effect.LoadingFailed)
//            .startWithValue(Effect.LoadingStarted)
//
//    private fun reduce(state: State, effect: Effect): State =
//        when (effect) {
//            is Effect.LoadingStarted -> state.copy(isLoading = true)
//            is Effect.LoadingFinished -> state.copy(isLoading = false, data = State.Data.SmartData(smartData = effect.smartData))
//            is Effect.LoadingFailed -> state.copy(isLoading = false, data = State.Data.Error)
//        }
//
//    private sealed class Effect {
//        object LoadingStarted : Effect()
//        data class LoadingFinished(val smartData: List<String>) : Effect()
//        object LoadingFailed : Effect()
//    }
//
//    interface Network {
//        fun load(): Maybe<String>
//    }
//
//    interface Parser {
//        fun parse(json: String): Maybe<List<String>>
//    }
//}