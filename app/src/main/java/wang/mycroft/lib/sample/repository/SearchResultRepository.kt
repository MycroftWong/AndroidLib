package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

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

    fun loadData(key: String, page: Int) {

        viewModelScope.launch {
            try {
                articleListLiveData.value = withContext(Dispatchers.IO) {
                    NetService.search(key, page)
                }
            } catch (e: Exception) {
                articleListLiveData.value = NetModel.error(e)
            }

        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}