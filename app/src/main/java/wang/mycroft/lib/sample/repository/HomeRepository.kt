package wang.mycroft.lib.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.Banner
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel
import wang.mycroft.lib.util.DisposableUtil

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

    private var disposable: Disposable? = null

    fun loadArticleList(page: Int) {
        disposable = NetService.getInstance().getHomeArticleList(page)
            .subscribe({
                articleListNetLiveData.value = it
            }, { throwable ->
                disposable = null
                articleListNetLiveData.value = NetModel.error(throwable)
            }, {
                disposable = null
            })
    }

    private val bannerListNetLiveData = MutableLiveData<NetModel<List<Banner>>>()

    private var bannerDisposable: Disposable? = null

    val bannerList: LiveData<ResultModel<List<Banner>>> =
        Transformations.map(bannerListNetLiveData) {
            return@map SimpleResultModel(it)
        }

    fun loadBannerList() {
        bannerDisposable = NetService.getInstance().homeBannerList
            .subscribe({
                bannerListNetLiveData.value = it
            }, { throwable ->
                bannerDisposable = null
                bannerListNetLiveData.value = NetModel.error(throwable)
            }, {
                bannerDisposable = null
            })
    }

    private val topArticleListNetLiveData = MutableLiveData<NetModel<List<Article>>>()

    val topArticleList: LiveData<ResultModel<List<Article>>> =
        Transformations.map(topArticleListNetLiveData) {
            SimpleResultModel(it)
        }

    private var topArticleDisposable: Disposable? = null

    fun loadTopArticleList() {
        topArticleDisposable = NetService.getInstance().topArticleList.subscribe({
            topArticleListNetLiveData.value = it
        }, {
            topArticleDisposable = null
            topArticleListNetLiveData.value = NetModel.error(it)
        }, {
            topArticleDisposable = null
        })
    }

    override fun onCleared() {
        super.onCleared()
        DisposableUtil.dispose(disposable)
        disposable = null

    }
}