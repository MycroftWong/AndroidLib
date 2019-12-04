package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13
 * @author: wangqiang
 */
class ArticleListRepository : ViewModel() {

    var articleUrl: String? = null

    private val articleListData = MutableLiveData<NetModel<ListData<Article>>>()

    val articleList: LiveData<ResultModel<ListData<Article>>> =
        Transformations.map(articleListData) {
            return@map SimpleResultModel<ListData<Article>>(it)
        }

    fun loadArticleList(page: Int) {
        viewModelScope.launch {
            try {
                articleListData.value =
                    withContext(Dispatchers.IO) { WebService.getArticleList(articleUrl!!, page) }
            } catch (e: Exception) {
                articleListData.value = NetModel.error(e)
            }
        }
    }
}