package com.jagadish.freshmart.utils

/**
 * Created by Jagadeesh on 01-03-2021.
 */
/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class SingleEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
