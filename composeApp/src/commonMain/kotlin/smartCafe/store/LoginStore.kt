package smartCafe.store

internal interface LoginStore {
    sealed class Intent {
        object Reload : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
    )
}