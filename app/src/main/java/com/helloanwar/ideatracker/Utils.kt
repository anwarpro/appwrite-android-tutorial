package com.helloanwar.ideatracker

sealed interface NetworkResource<out T> {
    data class Success<T>(val data: T) : NetworkResource<T>
    data class Error(val error: Throwable, val msg: String? = null) : NetworkResource<Nothing>
    object Loading : NetworkResource<Nothing>
    object Idle : NetworkResource<Nothing>
}