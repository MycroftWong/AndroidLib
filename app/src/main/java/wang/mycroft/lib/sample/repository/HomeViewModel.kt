package wang.mycroft.lib.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.Banner
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月14
 * @author: wangqiang
 */
class HomeViewModel : ViewModel() {

    companion object {
        private const val START_PAGE = 0
    }

    private val articleListNetLiveDataInternal =
        MutableLiveData<PagedResultModel<out List<Article>>>()

    val articleListLiveData: LiveData<PagedResultModel<out List<Article>>> =
        articleListNetLiveDataInternal

    private val bannerNetLiveDataInternal = MutableLiveData<List<Banner>>()

    val bannerLiveData: LiveData<List<Banner>> = bannerNetLiveDataInternal

    private var loading = false

    private var nextPage: Int = START_PAGE

    private val articleList = mutableListOf<Article>()

    fun loadArticleList(refresh: Boolean) {
        if (loading) {
            return
        }
        loading = true
        viewModelScope.launch {

            try {

                val bannerNetModelDefer: Deferred<NetModel<List<Banner>>> =
                    async(Dispatchers.IO) { WebService.getHomeBannerList() }
                val bannerNetModel = bannerNetModelDefer.await()

                if (bannerNetModel.code == CODE_SUCCESS) {
                    bannerNetLiveDataInternal.value = bannerNetModel.data ?: emptyList()
                }
            } catch (throwable: Throwable) {
                bannerNetLiveDataInternal.value = emptyList()
            }


            try {
                if (refresh) {
                    val articleListNetModelDefer: Deferred<NetModel<ListData<Article>>> =
                        async(Dispatchers.IO) { WebService.getHomeArticleList(START_PAGE) }
                    val topNetModelDefer: Deferred<NetModel<List<Article>>> =
                        async(Dispatchers.IO) { WebService.getTopArticleList() }

                    val articleListNetModel = articleListNetModelDefer.await()
                    val topNetModel = topNetModelDefer.await()

                    if (articleListNetModel.code != CODE_SUCCESS) {
                        articleListNetLiveDataInternal.value = PagedResultModel(
                            articleListNetModel.code,
                            articleListNetModel.message,
                            PagedResult(articleList, false)
                        )
                    } else {
                        articleList.clear()
                        articleList.addAll(topNetModel.data ?: emptyList())
                        val listData: ListData<Article> = articleListNetModel.data!!
                        articleList.addAll(listData.datas)
                        val pagedResult =
                            PagedResult(articleList, listData.curPage >= listData.pageCount)
                        articleListNetLiveDataInternal.value =
                            PagedResultModel(CODE_SUCCESS, articleListNetModel.message, pagedResult)
                    }
                } else {
                    val articleListNetModel = withContext(Dispatchers.IO) {
                        WebService.getHomeArticleList(nextPage)
                    }
                    if (articleListNetModel.code != CODE_SUCCESS) {
                        articleListNetLiveDataInternal.value = PagedResultModel(
                            articleListNetModel.code,
                            articleListNetModel.message,
                            PagedResult(articleList, false)
                        )
                    } else {
                        val listData: ListData<Article> = articleListNetModel.data!!
                        articleList.addAll(listData.datas)
                        val pagedResult =
                            PagedResult(articleList, listData.curPage >= listData.pageCount)
                        articleListNetLiveDataInternal.value =
                            PagedResultModel(CODE_SUCCESS, articleListNetModel.message, pagedResult)
                    }
                }
                loading = false
            } catch (throwable: Throwable) {
                loading = false
                articleListNetLiveDataInternal.value =
                    PagedResultModel(CODE_NET_ERROR, throwable.netMessage(), null)
            }
        }
    }

}