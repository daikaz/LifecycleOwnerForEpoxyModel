package com.vandai92.shared

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.airbnb.epoxy.DataBindingEpoxyModel

abstract class LifecyclerOwnerEpoxyModel : EpoxyModelWithHolder<LifecyclerOwnerEpoxyModel.DataBindingHolder?>() {

    override fun buildView(parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        val view = binding.root
        view.tag = binding
        return view
    }

    override fun bind(holder: DataBindingHolder) {
        holder.onBind()
        setDataBindingVariables(holder.dataBinding)
        holder.dataBinding!!.executePendingBindings()
    }

    /**
     * This is called when the model is bound to a view, and the view's variables should be updated
     * with the model's data. [ViewDataBinding.executePendingBindings] is called for you after
     * this method is run.
     *
     *
     * If you leave your class abstract and have a model generated for you via annotations this will
     * be implemented for you. However, you may choose to implement this manually if you like.
     */
    protected abstract fun setDataBindingVariables(binding: ViewDataBinding?)

    /**
     * Similar to [.setDataBindingVariables], but this method only binds
     * variables that have changed. The changed model comes from [.bind]. This will only be called if the model is used in an [EpoxyController]
     *
     *
     * If you leave your class abstract and have a model generated for you via annotations this will
     * be implemented for you. However, you may choose to implement this manually if you like.
     */
    protected fun setDataBindingVariables(
        dataBinding: ViewDataBinding?,
        previouslyBoundModel: EpoxyModel<*>?
    ) {
        setDataBindingVariables(dataBinding)
    }

    protected fun setDataBindingVariables(dataBinding: ViewDataBinding?, payloads: List<Any?>?) {
        setDataBindingVariables(dataBinding)
    }

    override fun unbind(holder: DataBindingHolder) {
        holder.dataBinding!!.unbind()
        holder.recycle()
    }

    override fun createNewHolder(): DataBindingHolder? {
        return DataBindingHolder()
    }

    class DataBindingHolder : EpoxyHolder(), LifecycleOwner {

        private val lifecycleRegistry by lazy { LifecycleRegistry(this) }
        var dataBinding: ViewDataBinding? = null
            private set

        override fun bindView(itemView: View) {
            dataBinding = itemView.tag as ViewDataBinding
        }
        
        fun recycle() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        fun onBind() {
            if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
            }
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }
    
}
