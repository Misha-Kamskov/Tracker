package com.example.tracker.mvi.states

interface ScreenState<V> {
    fun visit(screen: V)

}
