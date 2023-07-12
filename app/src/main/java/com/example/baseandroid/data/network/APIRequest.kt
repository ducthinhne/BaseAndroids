package com.example.baseandroid.data.network

import com.example.baseandroid.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

object APIPath {
    fun shopInfo(id: Int): String = "shop/${id}"
    fun common(): String = "common"
}

enum class HTTPError(val code: Int) {
    UNAUTHORISE(401)
}

@Singleton
class APIRequest @Inject constructor(
    val service: ApiService,
    val gson: Gson
) {

    companion object {
        const val BASE_URL = BuildConfig.API_ENDPOINT
    }

    inline fun <reified T> request(
        router: ApiRouter
    ): Flow<T> = flow { emit(gson.fromJson(getMethodCall(router).string())) }

    suspend fun getMethodCall(router: ApiRouter): ResponseBody = when (router.method) {
        HTTPMethod.GET -> service.get(router.url(), router.headers, router.parameters)
        HTTPMethod.POST -> service.post(router.url(), router.headers, router.parameters)
        HTTPMethod.PUT -> service.put(router.url(), router.headers, router.parameters)
        HTTPMethod.DELETE -> service.delete(router.url(), router.headers, router.parameters)
    }
}


//suspend fun Result<*>.onRetry(complete: () -> Unit) {
//    onFailure {
//        if (MainApplication.CONTEXT?.showTwoActionAlert(
//                "エラーが発生しました",
//                "もう一度やり直してください",
//                "リトライ",
//                "キャンセル"
//            ) == true
//        ) {
//            complete()
//        }
//    }
//}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)

data class ApiRouter(
    val path: String,
    val method: HTTPMethod = HTTPMethod.GET,
    val parameters: Parameters = hashMapOf(),
    val headers: Headers = JsonFormatter
)

val JsonFormatter = hashMapOf("accept" to "application/json", "Content-Type" to "application/json")

fun ApiRouter.url(): String = APIRequest.BASE_URL + path

enum class HTTPMethod {
    GET, POST, PUT, DELETE
}

inline fun Throwable.httpCode(): Int = (this as HttpException).code()