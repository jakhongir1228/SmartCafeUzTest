package smartCafe.store

import mvi.Store
import smartCafe.store.SmartStore.Intent
import smartCafe.store.SmartStore.State

internal interface SmartStore : Store<Intent,State>{
    sealed class Intent {
        object Reload : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val data:Data
    ) {
        sealed class Data{
            data class SmartData(val smartData: List<String> = emptyList()):Data()
            object Error:Data()
        }
    }
}