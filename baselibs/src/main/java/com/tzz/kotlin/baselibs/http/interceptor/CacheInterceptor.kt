package com.tzz.kotlin.baselibs.http.interceptor

import com.tzz.kotlin.baselibs.app.BaseApp
import com.tzz.kotlin.baselibs.http.constants.HttpConstant
import com.tzz.kotlin.baselibs.utils.NetWorkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @author Zero_Tzz
 * @date 2019-06-28 11:17
 * @description CacheInterceptor
 */
class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetWorkUtil.isConnected(BaseApp.instance)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        val response = chain.proceed(request)
        if (NetWorkUtil.isConnected(BaseApp.instance)) {
            val maxAge = 0
            // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
            response.newBuilder()
                    .header(HttpConstant.CACHE_CONTROL, "public, max-age=$maxAge")
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
        } else {
            // 无网络时，设置超时为4周  只对get有用,post没有缓冲
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                    .header(HttpConstant.CACHE_CONTROL, "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("nyn")
                    .build()
        }
        return response
    }
}