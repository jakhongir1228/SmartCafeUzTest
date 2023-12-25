package uz.smartcafe.kittens

import android.view.View
import kittens.KittenView.Model
import kittens.KittenView.Event
import mvi.AbstractMviView
import uz.smartcafe.R
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kittens.KittenView

internal class KittenViewImpl(root: View): AbstractMviView<Model, Event>(),KittenView {

    private val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swype_refresh)
    private val adapter = KittenAdapter()
    private val snackbar = Snackbar.make(root, R.string.error_loading_kittens, Snackbar.LENGTH_INDEFINITE)

    init {
        root.findViewById<RecyclerView>(R.id.recycler).adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            dispatch(Event.RefreshTriggered)
        }
    }

    override fun render(model: Model) {
        swipeRefreshLayout.isRefreshing = model.isLoading
        adapter.setUrls(model.imageUrls)
        if (model.isError){
            snackbar.show()
        }else{
            snackbar.dismiss()
        }
    }
}