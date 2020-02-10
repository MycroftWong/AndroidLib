package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class SearchResultViewModel : ViewModel() {

    private val articleListLiveData = MutableLiveData<NetModel<ListData<Article>>>()

    val articleList: LiveData<ResultModel<ListData<Article>>> =
        Transformations.map(articleListLiveData) { NetResultModel(it) }

    fun loadData(key: String, page: Int) {

        viewModelScope.launch {
            try {
                articleListLiveData.value = withContext(Dispatchers.IO) {
                    WebService.search(key, page)
                }
            } catch (throwable: Throwable) {
                articleListLiveData.value = NetModel(CODE_NET_ERROR, throwable.netMessage(), null)
            }
        }
    }
}