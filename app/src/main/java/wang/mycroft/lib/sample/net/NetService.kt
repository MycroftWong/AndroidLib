package wang.mycroft.lib.sample.net

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.mycroft.lib.net.RemoteService
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import wang.mycroft.lib.net.OkHttpClientMaker
import wang.mycroft.lib.net.RetrofitMaker
import wang.mycroft.lib.net.StringConverterFactory
import wang.mycroft.lib.net.cookiejar.PersistentCookieJar
import wang.mycroft.lib.net.cookiejar.cache.SetCookieCache
import wang.mycroft.lib.net.cookiejar.persistence.SharedPrefsCookiePersistor
import wang.mycroft.lib.sample.exception.NetDataException
import wang.mycroft.lib.sample.model.*
import wang.mycroft.lib.sample.service.FileServiceImpl
import java.util.*
import java.util.concurrent.TimeUnit

object NetService {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val service: IApiService

    init {

        val fileService = FileServiceImpl

//        val loggingInterceptor = HttpLoggingInterceptor(Logger { LogUtils.w(it) })
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

    private fun <T> handleResult(observable: Observable<NetModel<T>>): Observable<T> {
        return observable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map<T> { articleListModelNetModel ->
                if (articleListModelNetModel.errorCode == 0) {
                    return@map articleListModelNetModel.data
                } else {
                    throw NetDataException.newInstance(articleListModelNetModel.errorMsg)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun <T> handleResult2(observable: Observable<NetModel<T>>): Observable<NetModel<T>> {
        return observable.compose(async())
    }

    /**
     * 线程切换
     *
     * @param <T>
     * @return
    </T> */
    private fun <T> async(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}