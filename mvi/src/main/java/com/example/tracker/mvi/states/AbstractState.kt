package com.example.tracker.mvi.states

abstract class AbstractState<V, S> : ScreenState<V> {

    override fun visit(screen: V) {

    }

    @Suppress("UNCHECKED_CAST")
    open fun merge(prevState: S): S = this as S

}
