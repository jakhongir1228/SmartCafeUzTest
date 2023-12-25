package kittens.store

import mvi.Store
import kittens.store.KittenStore.Intent
import kittens.store.KittenStore.State

internal interface KittenStore : Store<Intent, State> {

    sealed class Intent {
        object Reload : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val data: Data = Data.Images()
    ) {

        sealed class Data {
            data class Images(val urls: List<String> = emptyList()) : Data()
            object Error : Data()
        }
    }

}