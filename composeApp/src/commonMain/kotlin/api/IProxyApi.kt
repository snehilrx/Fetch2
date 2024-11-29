package api

interface IProxyApi {
    suspend fun getReachableProxyFromList(): String
}