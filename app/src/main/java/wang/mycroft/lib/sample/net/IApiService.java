package wang.mycroft.lib.sample.net;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import wang.mycroft.lib.sample.model.Article;
import wang.mycroft.lib.sample.model.Banner;
import wang.mycroft.lib.sample.model.Category;
import wang.mycroft.lib.sample.model.HotKey;
import wang.mycroft.lib.sample.model.ListData;
import wang.mycroft.lib.sample.model.OfficialAccount;
import wang.mycroft.lib.sample.model.Project;
import wang.mycroft.lib.sample.model.Tools;

/**
 * @author mycroft
 */
public interface IApiService {

    /**
     * 知识体系
     *
     * @return
     */
    @GET("tree/json")
    Observable<NetModel<List<Category>>> getCategoryList();

    /**
     * 获取微信公众号
     *
     * @return
     */
    @GET("wxarticle/chapters/json")
    Observable<NetModel<List<OfficialAccount>>> getOfficialAccountList();

    /**
     * 通用的获取文章列表的接口
     *
     * @param page page
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<NetModel<ListData<Article>>> getHomeArticleList(@Path("page") int page);

    /**
     * 获取首页banner列表
     *
     * @return observable
     */
    @GET("banner/json")
    Observable<NetModel<List<Banner>>> getHomeBannerList();

    /**
     * 获取置顶文章
     *
     * @return observable
     */
    @GET("article/top/json")
    Observable<NetModel<List<Article>>> getTopArticleList();

    /**
     * 通用的获取文章列表的接口
     *
     * @param url url
     * @return
     */
    @GET
    Observable<NetModel<ListData<Article>>> getArticleList(@Url String url);

/*
    */
/**
     * 通用的获取文章列表的接口
     *
     * @param url url
     * @return
     *//*

    @GET
    Observable<NetModel<ListData<Article>>> getArticleList2(@Url String url);
*/


    /**
     * 获取导航数据
     *
     * @return
     */
    @GET("navi/json")
    Observable<NetModel<List<Tools>>> getToolList();

    /**
     * 获取项目分类
     *
     * @return
     */
    @GET("project/tree/json")
    Observable<NetModel<List<Project>>> getProjectList();

    /**
     * 热门搜索词
     *
     * @return
     */
    @GET("/hotkey/json")
    Observable<NetModel<List<HotKey>>> getHotKeyList();

    /**
     * 搜索
     *
     * @param key  搜索关键字
     * @param page page start with 0
     * @return
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Observable<NetModel<ListData<Article>>> search(@Field("k") String key, @Path("page") int page);
}
