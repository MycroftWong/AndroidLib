package wang.mycroft.lib.sample.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.vertical_refresh_recycler.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.repository.ArticleListViewModel
import wang.mycroft.lib.sample.repository.model.CODE_SUCCESS
import wang.mycroft.lib.sample.repository.model.PagedResult
import wang.mycroft.lib.sample.repository.model.PagedResultModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class ArticleListFragment : CommonFragment() {

    companion object {

        private const val ARGS_ARTICLE = "article.args"

        private const val ARGS_START_PAGE = "start_page.args"

        fun newInstance(url: String, startPage: Int): ArticleListFragment =
            ArticleListFragment().apply {
                arguments = bundleOf(
                    Pair(ARGS_ARTICLE, url),
                    Pair(ARGS_START_PAGE, startPage)
                )
            }
    }

    private val articleListViewModel: ArticleListViewModel by viewModels(
        factoryProducer = {
            ArticleListViewModel.Factory(
                arguments!!.getInt(ARGS_START_PAGE), arguments!!.getString(
                    ARGS_ARTICLE
                )!!
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleListViewModel.articleListLiveData.observe(this, observer)
    }

    private var adapter = ArticleListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.vertical_refresh_recycler, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)
    }

    override fun onResume() {
        super.onResume()
        if (adapter.articleList.isNullOrEmpty()) {
            refreshLayout.autoRefresh(0, 0, 1f, false)
        }
    }

    private val refreshLoadMoreListener = object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            articleListViewModel.loadArticleList(false)
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            articleListViewModel.loadArticleList(true)
        }
    }

    private val observer =
        Observer<PagedResultModel<out List<Article>>> { resultModel ->
            if (resultModel.code != CODE_SUCCESS) {
                ToastUtils.showShort(resultModel.message)
            } else {
                val pagedResult: PagedResult<out List<Article>> = resultModel.data!!
                refreshLayout.setNoMoreData(pagedResult.noMoreData)

                adapter.articleList = pagedResult.data
            }
            finishRefresh()
        }

    private fun finishRefresh() {
        with(refreshLayout) {
            if (isRefreshing) finishRefresh()
            if (isLoading) finishLoadMore()
        }
    }
}