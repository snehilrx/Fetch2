package model

sealed class ApiStateWithLoading<T> {
    class LOADING<T> : ApiStateWithLoading<T>()
    data class ERROR<T>(val message: String) : ApiStateWithLoading<T>()
    data class COMPLETED<T>(val value: T) : ApiStateWithLoading<T>()
}