package com.tzz.kotlin.baselibs.http.interceptor

import com.tzz.kotlin.baselibs.http.constants.HttpConstant
import com.tzz.kotlin.baselibs.utils.Preference
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @author Zero_Tzz
 * @date 2019-06-28 11:39
 * @description HeaderInterceptor
 */
class HeaderInterceptor : Interceptor {

    /**
     * token
     */
    private var token: String by Preference(HttpConstant.TOKEN_KEY, "")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
        // .header("token", token)
        // .method(request.method(), request.body())

        val domain = request.url().host()
        val url = request.url().toString()
        if (domain.isNotEmpty()) {
            val spDomain: String by Preference(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(HttpConstant.COOKIE_NAME, cookie)
            }
        }

        return chain.proceed(builder.build())
    }

}