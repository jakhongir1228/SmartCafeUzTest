package kittens

import mvi.MviView
import kittens.KittenView.Model
import kittens.KittenView.Event
interface KittenView : MviView<Model, Event> {

    data class Model(
        val isLoading: Boolean,
        val isError: Boolean,
        val imageUrls: List<String>
    )

    sealed class Event {
        object RefreshTriggered : Event()
    }
}