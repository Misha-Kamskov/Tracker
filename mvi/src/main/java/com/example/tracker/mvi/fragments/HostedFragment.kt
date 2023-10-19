package com.example.tracker.mvi.fragments

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.tracker.mvi.states.ScreenState
import java.lang.reflect.ParameterizedType

abstract class HostedFragment<
        VIEW : FragmentContract.View,
        VIEW_MODEL : FragmentContract.ViewModel<VIEW>,
        HOST : FragmentContract.Host>
    : NavHostFragment(), FragmentContract.View, Observer<ScreenState<VIEW>>,
    LifecycleEventObserver {

    protected var model: VIEW_MODEL? = null
        private set

    protected var fragmentHost: HOST? = null
        private set

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentHost = try {
            context as HOST
        } catch (e: Throwable) {
            val hostClassName = ((javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[1] as Class<*>).canonicalName
            throw RuntimeException(
                "Activity must implement $hostClassName to attach ${this.javaClass.simpleName}",
                e
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentHost = null
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setModel(createModel())
        lifecycle.addObserver(this)
        model?.getStateObservable()?.observe(this, this)
        model?.getEffectObservable()?.observe(this) { it.visit(this@HostedFragment as VIEW) }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        model?.onStateChanged(event)

        if (lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            lifecycle.removeObserver(this)
            model?.getEffectObservable()?.removeObserver(this)
            model?.getStateObservable()?.removeObserver(this)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onChanged(value: ScreenState<VIEW>) {
        value.visit(this@HostedFragment as VIEW)
    }

    protected abstract fun createModel(): VIEW_MODEL

    protected fun setModel(model: VIEW_MODEL) {
        this.model = model
    }

}
