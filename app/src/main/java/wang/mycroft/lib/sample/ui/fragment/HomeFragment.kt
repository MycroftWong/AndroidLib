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
import wang.mycroft.lib.sample.repository.HomeRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.activity.WebViewActivity
import wang.mycroft.lib.sample.ui.adapter.recycler.ArticleListAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder
import java.util.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月14
 * @author: wangqiang
 */
class HomeFragment : CommonFragment() {

    companion object {
        private const val STATE_NEXT_PAGE = "next_page.state"

        private const val START_PAGE = 0
    }

    private val homeRepository by lazy {
        ViewModelProvider(this).get(HomeRepository::class.java)
    }

    private var nextPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nextPage = savedInstanceState?.getInt(STATE_NEXT_PAGE) ?: START_PAGE

        homeRepository.articleList.observe(
            this,
            Observer<ResultModel<ListData<Article>>> { listDataResultModel ->
                if (listDataResultModel.errorCode != 0) {
                    if (articleTypeModels.isEmpty()) {
                        holder?.showLoadFailed()
                    } else {
                        ToastUtils.showShort(listDataResultModel.errorMsg)
                    }
                } else {
                    holder?.showLoadSuccess()

                    val listData = listDataResultModel.data

                    if (listData.curPage == START_PAGE) {
                        articleTypeModels.clear()
                    }
                    nextPage = listData.curPage + 1

                    for (item in listData.datas) {
                        articleTypeModels.add(ArticleTypeModel(item))
                    }
                    adapter?.notifyDataSetChanged()
                }
                finishRefresh()
            })
    }

    private val articleTypeModels = ArrayList<ArticleTypeModel>()

    private var adapter: ArticleListAdapter? = null

    private var holder: LoadingHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vertical_refresh_recycler, container, false)
        holder = Loading.getDefault().wrap(view).withRetry {
            holder?.showLoading()
            loadData(START_PAGE)
        }

        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArticleListAdapter(articleTypeModels)

        adapter!!.setOnItemClickListener { _, _, position ->
            startActivity(
                WebViewActivity.getIntent(
                    context!!,
                    articleTypeModels[position].article
                )
            )
        }

        recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
            this.adapter = this@HomeFragment.adapter
        }

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)

        if (articleTypeModels.isEmpty()) {
            holder?.showLoading()
        } else {
            holder?.showLoadSuccess()
        }
    }

    override fun onResume() {
        super.onResume()
        if (articleTypeModels.isEmpty()) {
            loadData(START_PAGE)
        }
    }

    private var isLoading = false

    private fun loadData(page: Int) {
        if (!isLoading) {
            holder?.showLoading()
            isLoading = true
            homeRepository.loadArticleList(page)
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
            loadData(START_PAGE)
        }
    }
}