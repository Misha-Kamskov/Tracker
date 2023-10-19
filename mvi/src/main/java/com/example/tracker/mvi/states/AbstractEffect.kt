package com.example.tracker.mvi.states

abstract class AbstractEffect<T> : ScreenState<T> {
    var isHandled = false

    override fun visit(screen: T) {
        if (!isHandled) {
            handle(screen)
            isHandled = true
        }
    }

    open fun handle(screen: T) {

    }

}
