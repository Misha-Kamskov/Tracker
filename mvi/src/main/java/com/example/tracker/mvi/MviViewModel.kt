package com.example.tracker.mvi

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracker.mvi.fragments.FragmentContract
import com.example.tracker.mvi.states.AbstractState
import com.example.tracker.mvi.states.ScreenState

abstract class MviViewModel<V, STATE : AbstractState<V, STATE>> : ViewModel(),
    FragmentContract.ViewModel<V> {
    private val stateHolder = MutableLiveData<ScreenState<V>>()
    private val effectHolder = MutableLiveData<ScreenState<V>>()

    override fun getStateObservable() = stateHolder

    override fun getEffectObservable() = effectHolder

    @Suppress("UNCHECKED_CAST")
    protected fun setState(state: STATE) {
        stateHolder.value = stateHolder.value?.let {
            state.merge(it as STATE)
        } ?: state
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getState() = stateHolder.value as STATE?

    protected fun setEffect(action: ScreenState<V>) {
        effectHolder.value = action
    }

    @CallSuper
    override fun onStateChanged(event: Lifecycle.Event) {
    }

}
