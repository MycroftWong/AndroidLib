package wang.mycroft.lib.sample.net

import retrofit2.http.*
import wang.mycroft.lib.sample.model.*
import wang.mycroft.lib.sample.repository.model.NetModel

interface IApiService {

    /**
     * 知识体系
     *
     * @return
     */
    @GET("tree/json")
    suspend fun getCategoryList(): NetModel<List<Category>>

    /**
     * 获取微信公众号
     *
     * @return
     */
    @GET("wxarticle/chapters/json")
    suspend fun getOfficialAccountList(): NetModel<List<OfficialAccount>>

    /**
     * 通用的获取文章列表的接口
     *
     * @param page page
     * @return
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticleList(@Path("page") page: Int): NetModel<ListData<Article>>

    /**
     * 获取首页banner列表
     *
     * @return observable
     */
    @GET("banner/json")
    suspend fun getHomeBannerList(): NetModel<List<Banner>>

    /**
     * 获取置顶文章
     *
     * @return observable
     */
    @GET("article/top/json")
    suspend fun getTopArticleList(): NetModel<List<Article>>

    /**
     * 通用的获取文章列表的接口
     *
     * @param url url
     * @return
     */
    @GET
    suspend fun getArticleList(@Url url: String): NetModel<ListData<Article>>

    /**
     * 获取导航数据
     *
     * @return
     */
    @GET("navi/json")
    suspend fun getNavigationList(): NetModel<List<Navigation>>

    /**
     * 获取项目分类
     *
     * @return
     */
    @GET("project/tree/json")
    suspend fun getProjectList(): NetModel<List<Project>>

    /**
     * 热门搜索词
     *
     * @return
     */
    @GET("/hotkey/json")
    suspend fun getHotKeyList(): NetModel<List<HotKey>>

    /**
     * 搜索
     *
     * @param key  搜索关键字
     * @param page page start with 0
     * @return
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun search(@Field("k") key: String, @Path("page") page: Int): NetModel<ListData<Article>>
}
