package wang.mycroft.lib.sample.net

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import wang.mycroft.lib.net.HttpConstants
import java.io.IOException

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
object CacheInterceptor : Interceptor {

    private val userAgent = "android/" + AppUtils.getAppVersionName()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        // TODO 每次请求都检测网络不友好，应该使用监听
        val isNetworkConnected = NetworkUtils.isConnected()

        // 没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
        val requestBuilder = chain.request().newBuilder()
            .header("User-Agent", userAgent)
        if (!isNetworkConnected) {
            requestBuilder
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        var response = chain.proceed(requestBuilder.build())
        if (isNetworkConnected) {
            // 有网失效一分钟
            val maxAge = TimeConstants.MIN
            response = response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader(HttpConstants.CACHE_CONTROL)
                .header(HttpConstants.CACHE_CONTROL, "public, max-age=$maxAge")
                .build()
        } else {
            // 没网失效一天
            val maxStale = TimeConstants.DAY
            response = response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader(HttpConstants.CACHE_CONTROL)
                .header(HttpConstants.CACHE_CONTROL, "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
        return response
    }
}