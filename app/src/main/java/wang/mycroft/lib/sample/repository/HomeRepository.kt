package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.Banner
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月14
 * @author: wangqiang
 */
class HomeRepository : ViewModel() {

    private val articleListNetLiveData = MutableLiveData<NetModel<ListData<Article>>>()

    val articleList: LiveData<ResultModel<ListData<Article>>> =
        Transformations.map(articleListNetLiveData) {
            return@map SimpleResultModel<ListData<Article>>(it)
        }

    fun loadArticleList(page: Int) {

        viewModelScope.launch {
            try {
                articleListNetLiveData.value = withContext(Dispatchers.IO) {
                    NetService.getHomeArticleList(page)
                }
            } catch (e: Exception) {
                articleListNetLiveData.value = NetModel.error(e)
            }
        }
    }

    private val bannerListNetLiveData = MutableLiveData<NetModel<List<Banner>>>()

    val bannerList: LiveData<ResultModel<List<Banner>>> =
        Transformations.map(bannerListNetLiveData) {
            return@map SimpleResultModel(it)
        }

    fun loadBannerList() {
        viewModelScope.launch {
            try {
                bannerListNetLiveData.value = withContext(Dispatchers.IO) {
                    NetService.getHomeBannerList()
                }
            } catch (e: Exception) {
                bannerListNetLiveData.value = NetModel.error(e)
            }
        }
    }

    private val topArticleListNetLiveData = MutableLiveData<NetModel<List<Article>>>()

    val topArticleList: LiveData<ResultModel<List<Article>>> =
        Transformations.map(topArticleListNetLiveData) {
            SimpleResultModel(it)
        }

    fun loadTopArticleList() {
        viewModelScope.launch {
            try {
                topArticleListNetLiveData.value = NetService.getTopArticleList()
            } catch (e: Exception) {
                topArticleListNetLiveData.value = NetModel.error(e)
            }
        }
    }
}