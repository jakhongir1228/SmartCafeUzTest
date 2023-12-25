package kittens.integration

import kittens.KittenView.Model
import kittens.KittenView.Event
import kittens.store.KittenStore.State
import kittens.store.KittenStore.Intent

internal fun State.toModel(): Model =
    Model(
        isLoading = isLoading,
        isError = when (data) {
            is State.Data.Images -> false
            is State.Data.Error -> true
        },
        imageUrls = when (data) {
            is State.Data.Images -> data.urls
            is State.Data.Error -> emptyList()
        }
    )

internal fun Event.toIntent(): Intent =
    when (this) {
        is Event.RefreshTriggered -> Intent.Reload
    }