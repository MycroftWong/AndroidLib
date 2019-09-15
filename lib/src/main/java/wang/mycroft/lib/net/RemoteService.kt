package com.mycroft.lib.net

import android.util.ArrayMap
import com.blankj.utilcode.util.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import wang.mycroft.lib.net.OkHttpClientMaker
import wang.mycroft.lib.net.RetrofitMaker
import wang.mycroft.lib.net.StringConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 简化{@link Retrofit}的构造工作
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13
 * @author: wangqiang
 */
class RemoteService private constructor(maker: OkHttpClientMaker?) {

    companion object {
        private var remoteService: RemoteService? = null

        fun init(maker: OkHttpClientMaker?): RemoteService {
            if (remoteService == null) {
                remoteService = RemoteService(maker)
            }

            return remoteService!!
        }

        fun getImpl(): RemoteService {
            checkNotNull(remoteService) { "RemoteService has not been initialized!" }
            return remoteService!!
        }
    }

    private val httpClient: OkHttpClient

    init {
        if (maker != null) {
            httpClient = maker.makeOkHttpClient()
        } else {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient = OkHttpClient.Builder()
                .cache(Cache(File(Utils.getApp().getCacheDir(), "net"), 10L shl 20))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }
    }

    private val retrofitMap = ArrayMap<String, Retrofit>(3)


    /**
     * 构造[Retrofit]
     *
     * @param baseUrl 构造[Retrofit]时的base url
     */
    fun initRetrofit(baseUrl: String, maker: RetrofitMaker?) {
        if (retrofitMap.containsKey(baseUrl)) {
            return
        }
        val retrofit: Retrofit
        if (maker == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } else {
            retrofit = maker.makeRetrofit(baseUrl)
        }
        retrofitMap[baseUrl] = retrofit
    }

    /**
     * 获取[OkHttpClient], 可提供其他模块复用
     *
     * @return [OkHttpClient]
     */
    fun getHttpClient(): OkHttpClient {
        return httpClient
    }

    /**
     * 使用base url对应的retrofit构造 api service 接口
     *
     * @param klazz api 接口类类对象
     * @param <T>   api 接口类型
     * @return api 接口实现类
    </T> */
    fun <T> createApiService(baseUrl: String, klazz: Class<T>): T {
        val retrofit = retrofitMap[baseUrl]
        return if (retrofit != null) {
            retrofit.create(klazz)
        } else {
            throw IllegalStateException("It hasn't been a Retrofit to handle '$baseUrl'.")
        }
    }

}