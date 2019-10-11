package wang.mycroft.lib.sample.net;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.mycroft.lib.net.RemoteService;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import wang.mycroft.lib.net.StringConverterFactory;
import wang.mycroft.lib.net.cookiejar.PersistentCookieJar;
import wang.mycroft.lib.net.cookiejar.cache.SetCookieCache;
import wang.mycroft.lib.net.cookiejar.persistence.SharedPrefsCookiePersistor;
import wang.mycroft.lib.sample.exception.NetDataException;
import wang.mycroft.lib.sample.model.Article;
import wang.mycroft.lib.sample.model.Banner;
import wang.mycroft.lib.sample.model.Category;
import wang.mycroft.lib.sample.model.HotKey;
import wang.mycroft.lib.sample.model.ListData;
import wang.mycroft.lib.sample.model.OfficialAccount;
import wang.mycroft.lib.sample.model.Project;
import wang.mycroft.lib.sample.model.Tools;
import wang.mycroft.lib.sample.service.FileServiceImpl;
import wang.mycroft.lib.sample.service.IFileService;

/**
 * net service
 *
 * @author Mycroft Wong
 * @date 2019年10月10日
 */
public final class NetService {

    public static NetService getInstance() {
        return Holder.netService;
    }

    private static class Holder {
        private static NetService netService = new NetService();
    }

    private static final String BASE_URL = "https://www.wanandroid.com/";

    private NetService() {

        IFileService fileService = FileServiceImpl.INSTANCE;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LogUtils::w);
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        CookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(Utils.getApp()));

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .cache(new Cache(fileService.getNetCacheDir(), 10 << 20))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
//                .addNetworkInterceptor(CacheInterceptor.INSTANCE)
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        RemoteService.Companion.init(() -> httpClient);
        RemoteService.Companion.getImpl().initRetrofit(BASE_URL, baseUrl -> new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(LiveDataCallFactory.Companion.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build());

        service = RemoteService.Companion.getImpl().createApiService(BASE_URL, IApiService.class);
    }

    private final IApiService service;

    public Observable<NetModel<ListData<Article>>> getHomeArticleList(int page) {
        Observable<NetModel<ListData<Article>>> observable = service.getHomeArticleList(page);
        return handleResult2(observable);
    }

    public Observable<NetModel<List<Banner>>> getHomeBannerList() {
        Observable<NetModel<List<Banner>>> observable = service.getHomeBannerList();
        return handleResult2(observable);
    }

    public Observable<NetModel<List<Article>>> getTopArticleList() {
        Observable<NetModel<List<Article>>> observable = service.getTopArticleList();
        return handleResult2(observable);
    }

    public Observable<NetModel<ListData<Article>>> getArticleList(String url, int page) {
        Observable<NetModel<ListData<Article>>> observable = service.getArticleList(String.format(Locale.US, url, page));
        return handleResult2(observable);
    }

    public Observable<NetModel<List<Category>>> getCategoryList() {
        Observable<NetModel<List<Category>>> observable = service.getCategoryList();
        return handleResult2(observable);
    }

    public Observable<List<OfficialAccount>> getOfficialAccountList() {
        Observable<NetModel<List<OfficialAccount>>> observable = service.getOfficialAccountList();
        return handleResult(observable);
    }

    public Observable<List<Tools>> getToolList() {
        Observable<NetModel<List<Tools>>> observable = service.getToolList();
        return handleResult(observable);
    }

    public Observable<List<Project>> getProjectList() {
        Observable<NetModel<List<Project>>> observable = service.getProjectList();
        return handleResult(observable);
    }

    public Observable<NetModel<List<HotKey>>> getHotKeyList() {
        Observable<NetModel<List<HotKey>>> observable = service.getHotKeyList();
        return handleResult2(observable);
    }

    public Observable<NetModel<ListData<Article>>> search(String key, int page) {
        Observable<NetModel<ListData<Article>>> observable = service.search(key, page);
        return handleResult2(observable);
    }

    private static <T> Observable<T> handleResult(Observable<NetModel<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(articleListModelNetModel -> {
                    if (articleListModelNetModel.getErrorCode() == 0) {
                        return articleListModelNetModel.getData();
                    } else {
                        throw NetDataException.newInstance(articleListModelNetModel.getErrorMsg());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static <T> Observable<NetModel<T>> handleResult2(Observable<NetModel<T>> observable) {
        return observable.compose(async());
    }

    /**
     * 线程切换
     *
     * @param <T>
     * @return
     */
    private static <T> ObservableTransformer<T, T> async() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
