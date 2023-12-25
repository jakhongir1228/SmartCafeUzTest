package uz.smartcafe.kittens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kittens.KittenComponent
import uz.smartcafe.R

class KittenFragment : Fragment(R.layout.kitten_fragment){
    private lateinit var component: KittenComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = KittenComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.onViewCreated(KittenViewImpl(view))
    }

    override fun onStart() {
        super.onStart()
        component.onStart()
    }

    override fun onStop() {
        component.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        component.onViewDestroyed()
        super.onDestroyView()
    }

    override fun onDestroy() {
        component.onDestroy()
        super.onDestroy()
    }
}