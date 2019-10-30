package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.vertical_refresh_recycler.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ArticleTypeModel
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.repository.ArticleListRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.activity.ArticleWebViewActivity
import wang.mycroft.lib.sample.ui.adapter.recycler.ArticleListAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import java.util.*

private const val ARGS_ARTICLE = "article.args"
private const val ARGS_START_PAGE = "start_page.args"

private const val SAVED_ARTICLES = "articleList.saved"

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class ArticleListFragment : CommonFragment() {

    companion object {

        fun newInstance(url: String, startPage: Int): ArticleListFragment {
            return ArticleListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS_ARTICLE, url)
                    putInt(ARGS_START_PAGE, startPage)
                }
            }
        }
    }

    private var articleUrl: String? = null
    private var startPage: Int = 0

    private val articleListRepository: ArticleListRepository by lazy {
        ViewModelProvider(this)[ArticleListRepository::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null == savedInstanceState) {
            articleUrl = arguments!!.getString(ARGS_ARTICLE)
            startPage = arguments!!.getInt(ARGS_START_PAGE)
        } else {
            articleUrl = savedInstanceState.getString(ARGS_ARTICLE)
            startPage = savedInstanceState.getInt(ARGS_START_PAGE)
            val articles =
                savedInstanceState.getParcelableArrayList<ArticleTypeModel>(SAVED_ARTICLES)
            if (null != articles && articles.isNotEmpty()) {
                articleTypeModels.addAll(articles)
            }
        }

        nextPage = startPage

        articleListRepository.articleUrl = articleUrl
        articleListRepository.articleList.observe(this, observer)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARGS_ARTICLE, articleUrl)
        outState.putInt(ARGS_START_PAGE, startPage)
        outState.putParcelableArrayList(SAVED_ARTICLES, articleTypeModels)
    }

    private val articleTypeModels = ArrayList<ArticleTypeModel>()

    private var adapter: ArticleListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vertical_refresh_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )

        adapter = ArticleListAdapter(articleTypeModels)
        adapter!!.setOnItemClickListener { _, _, position ->
            startActivity(
                ArticleWebViewActivity.getIntent(
                    context!!,
                    articleTypeModels[position].article.title,
                    articleTypeModels[position].article.link
                )
            )
        }
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)
    }

    override fun onResume() {
        super.onResume()
        if (articleTypeModels.isEmpty()) {
            refreshLayout.autoRefresh(0, 0, 1f, false)
        }
    }

    private var nextPage: Int = 0

    private var isLoading = false

    private fun loadData(page: Int) {
        if (!isLoading) {
            isLoading = true
            articleListRepository.loadArticleList(page)
        }
    }

    private fun finishRefresh() {
        isLoading = false
        refreshLayout.run {
            if (isRefreshing) {
                finishRefresh()
            }
            if (isLoading) {
                finishLoadMore()
            }
        }
    }

    override fun onDestroyView() {
        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null

        super.onDestroyView()
    }

    private val refreshLoadMoreListener = object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            loadData(nextPage)
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            loadData(startPage)
        }
    }

    private val observer = Observer<ResultModel<ListData<Article>>> { resultModel ->
        if (resultModel.errorCode != ResultModel.CODE_SUCCESS) {
            ToastUtils.showShort(resultModel.errorMsg)
        } else {
            val listData = resultModel.data
            if (listData.curPage == startPage) {
                articleTypeModels.clear()
            }
            nextPage = listData.curPage + 1

            refreshLayout.setNoMoreData(listData.curPage >= listData.pageCount)

            for (item in listData.datas) {
                articleTypeModels.add(ArticleTypeModel(item))
            }
            adapter!!.notifyDataSetChanged()
        }
        finishRefresh()
    }

}