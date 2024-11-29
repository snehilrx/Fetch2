package model

sealed class ApiState<T> {
    data class ERROR<T>(val message: String) : ApiState<T>()
    data class COMPLETED<T>(val value: T) : ApiState<T>()
}

