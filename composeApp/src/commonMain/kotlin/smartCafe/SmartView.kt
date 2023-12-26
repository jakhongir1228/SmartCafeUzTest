package smartCafe

import mvi.MviView
import smartCafe.SmartView.Model
import smartCafe.SmartView.Event

interface SmartView: MviView<Model,Event> {

    data class Model(
        val isLoading: Boolean,
        val isError: Boolean,
        val authorization: Any? = null,
        val resultCode: Int? = null,
        val resultNote: String? = null,
        val actionID: Int? = null,
        val userID: Int? = null,
        val items: List<String>
    )
    sealed class Event{
        object RefreshTriggered : Event()
    }
}