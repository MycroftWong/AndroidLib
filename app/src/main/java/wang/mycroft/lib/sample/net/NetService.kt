package wang.mycroft.lib.sample.net

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wang.mycroft.lib.net.OkHttpClientMaker
import wang.mycroft.lib.net.RemoteService
import wang.mycroft.lib.net.RetrofitMaker
import wang.mycroft.lib.net.StringConverterFactory
import wang.mycroft.lib.net.cookiejar.PersistentCookieJar
import wang.mycroft.lib.net.cookiejar.cache.SetCookieCache
import wang.mycroft.lib.net.cookiejar.persistence.SharedPrefsCookiePersistor
import wang.mycroft.lib.sample.model.*
import wang.mycroft.lib.sample.service.FileServiceImpl
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit

object NetService {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val service: IApiService

    init {

        val fileService = FileServiceImpl

        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                LogUtils.w(message)
            }
        })


        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(Utils.getApp())
        )

        val httpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .cache(Cache(fileService.netCacheDir, (10 shl 20).toLong()))
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            //                .addNetworkInterceptor(CacheInterceptor.INSTANCE)
            .addNetworkInterceptor(loggingInterceptor)
            .build()

        RemoteService.init(OkHttpClientMaker { httpClient })

        RemoteService.getImpl().initRetrofit(BASE_URL, RetrofitMaker { baseUrl ->
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        })

        service = RemoteService.getImpl().createApiService(BASE_URL, IApiService::class.java)
    }

    suspend fun getHomeArticleList(page: Int): NetModel<ListData<Article>> {
        return service.getHomeArticleList(page)
    }

    suspend fun getHomeBannerList(): NetModel<List<Banner>> {
        return service.getHomeBannerList()
    }

    suspend fun getTopArticleList(): NetModel<List<Article>> {
        return service.getTopArticleList()
    }

    suspend fun getArticleList(url: String, page: Int): NetModel<ListData<Article>> {
        return service.getArticleList(String.format(Locale.US, url, page))
    }

    suspend fun getCategoryList(): NetModel<List<Category>> {
        return service.getCategoryList()
    }

    suspend fun getOfficialAccountList(): NetModel<List<OfficialAccount>> {
        return service.getOfficialAccountList()
    }

    suspend fun getToolList(): NetModel<List<Tools>> {
        return service.getToolList()
    }

    suspend fun getProjectList(): NetModel<List<Project>> {
        return service.getProjectList()
    }

    suspend fun getHotKeyList(): NetModel<List<HotKey>> {
        return service.getHotKeyList()
    }

    suspend fun search(key: String, page: Int): NetModel<ListData<Article>> {
        return service.search(key, page)
    }
}