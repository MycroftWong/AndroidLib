package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13
 * @author: wangqiang
 */
class ArticleListViewModel(
    // 开始页
    private val startPage: Int,
    // 前缀
    private val articleUrl: String
) : ViewModel() {

    class Factory(private val startPage: Int, private val articleUrl: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Int::class.java, String::class.java)
                .newInstance(startPage, articleUrl)
        }
    }

    private val articleListLiveDataInternal = MutableLiveData<PagedResultModel<out List<Article>>>()

    private val articleList = mutableListOf<Article>()

    val articleListLiveData: LiveData<PagedResultModel<out List<Article>>> =
        articleListLiveDataInternal

    /**
     * 下一页的页码
     */
    private var nextPage: Int = 0

    /**
     * 是否正在加载
     */
    private var isLoading = false

    fun loadArticleList(refresh: Boolean) {
        if (isLoading) {
            return
        }
        isLoading = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val netModel =
                        WebService.getArticleList(articleUrl, if (refresh) startPage else nextPage)

                    if (netModel.code != CODE_SUCCESS) {
                        articleListLiveDataInternal.postValue(
                            PagedResultModel(
                                netModel.code, netModel.message, PagedResult(articleList, false)
                            )
                        )
                    } else {
                        val listData = netModel.data!!

                        if (refresh) {
                            articleList.clear()
                        }
                        articleList.addAll(listData.datas)

                        nextPage = listData.curPage + 1
                        articleListLiveDataInternal.postValue(
                            PagedResultModel(
                                CODE_SUCCESS,
                                netModel.message,
                                PagedResult(articleList, listData.curPage >= listData.pageCount)
                            )
                        )
                    }
                    isLoading = false
                } catch (throwable: Throwable) {
                    isLoading = false
                    articleListLiveDataInternal.postValue(
                        PagedResultModel(
                            CODE_NET_ERROR, throwable.netMessage(), PagedResult(articleList, false)
                        )
                    )
                }
            }
        }
    }

}