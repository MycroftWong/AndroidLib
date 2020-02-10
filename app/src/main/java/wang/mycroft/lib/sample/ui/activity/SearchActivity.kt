package wang.mycroft.lib.sample.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.vertical_refresh_recycler.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ArticleTypeModel
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.repository.SearchResultViewModel
import wang.mycroft.lib.sample.repository.model.CODE_SUCCESS
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.adapter.recycler.ArticleListAdapter

/**
 * 搜索页面
 *
 * @blog https://blog.mycroft.wang/
 * @author Mycroft Wong
 * @date 2019年12月17日
 */
class SearchActivity : CommonActivity() {

    companion object {

        private const val START_PAGE = 0

        private const val EXTRA_KEYWORD = "keyword.extra"

        fun getIntent(context: Context, keyword: String): Intent =
            Intent(context, SearchActivity::class.java).apply {
                putExtra(EXTRA_KEYWORD, keyword)
            }
    }

    override fun getResId(): Int {
        return R.layout.activity_search
    }

    private lateinit var keyword: String

    private val searchResultViewModel: SearchResultViewModel by viewModels()

    override fun initFields(savedInstanceState: Bundle?) {
        keyword = intent.getStringExtra(EXTRA_KEYWORD)

        searchResultViewModel.articleList.observe(this, articleListObserver)
    }

    private val searchResultList = ArrayList<ArticleTypeModel>()

    private val adapter: ArticleListAdapter by lazy {
        ArticleListAdapter(searchResultList)
    }

    override fun initViews() {
        backImage.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
            overridePendingTransition(0, 0)
        }
        searchText.text = keyword

        searchText.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
            overridePendingTransition(0, 0)
        }

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)

        adapter.onItemClickListener = onItemClickListener
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    override fun loadData() {
        refreshLayout.autoRefresh(0, 0, 1f, false)
    }

    private var requestingPage = START_PAGE

    private var currentPage = START_PAGE

    private val refreshLoadMoreListener = object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            loadData(currentPage + 1)
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            loadData(START_PAGE)
        }
    }

    private var isLoading = false

    private fun loadData(page: Int) {
        if (isLoading) {
            return
        }

        requestingPage = page
        isLoading = true
        searchResultViewModel.loadData(keyword, page)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        startActivity(
            ArticleWebViewActivity.getIntent(
                this,
                searchResultList[position].article.title,
                searchResultList[position].article.link
            )
        )
    }

    private val articleListObserver = Observer<ResultModel<ListData<Article>>> { resultModel ->
        finishRefresh()
        if (resultModel.code != CODE_SUCCESS) {
            ToastUtils.showShort(resultModel.message)
        } else {
            val listData = resultModel.data!!
            currentPage = requestingPage

            if (requestingPage == START_PAGE) {
                searchResultList.clear()
            }

            listData.datas.forEach {
                searchResultList.add(ArticleTypeModel(it))
            }

            adapter.notifyDataSetChanged()
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
}
