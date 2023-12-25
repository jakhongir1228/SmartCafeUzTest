package mvi

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableObserver
import com.badoo.reaktive.subject.behavior.BehaviorSubject

class StoreHelper<in Intent:Any,out State:Any,in Effect:Any> (
    initialState: State,
    private val actor: Actor<State,Intent,Effect>,
    private val reducer: Reducer<State,Effect>,
):Observable<State>, DisposableScope by DisposableScope() {

    private val subject = BehaviorSubject(initialState)
    val state: State get() = subject.value

    fun onIntent(intent: Intent) {
        actor(subject.value, intent).subscribeScoped(onNext = ::onEffect)
    }

    fun onEffect(effect: Effect) {
        subject.onNext(reducer(subject.value, effect))
    }

    override fun subscribe(observer: ObservableObserver<State>) {
        subject.subscribe(observer)
    }
}