package wang.mycroft.lib.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel
import wang.mycroft.lib.util.DisposableUtil

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class SearchResultRepository : ViewModel() {

    private val articleListLiveData = MutableLiveData<NetModel<ListData<Article>>>()

    val articleList: LiveData<ResultModel<ListData<Article>>> =
        Transformations.map(articleListLiveData) {
            SimpleResultModel<ListData<Article>>(it)
        }
    private var disposable: Disposable? = null

    fun loadData(key: String, page: Int) {
        disposable = NetService.getInstance().search(key, page)
            .subscribe({ listData ->
                articleListLiveData.value = listData
            }, { throwable ->
                disposable = null
                articleListLiveData.value = NetModel<ListData<Article>>(-1, throwable.message, null)
            }, { disposable = null })
    }

    override fun onCleared() {
        super.onCleared()
        DisposableUtil.dispose(disposable)
        disposable = null
    }
}