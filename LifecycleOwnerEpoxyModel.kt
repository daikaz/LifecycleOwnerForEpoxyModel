package com.vandai92.shared

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.airbnb.epoxy.DataBindingEpoxyModel

abstract class LifecyclerOwnerEpoxyModel : DataBindingEpoxyModel(), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onViewAttachedToWindow(holder: DataBindingHolder) {
        super.onViewAttachedToWindow(holder)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onViewDetachedFromWindow(holder: DataBindingHolder) {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        super.onViewDetachedFromWindow(holder)
    }
}
