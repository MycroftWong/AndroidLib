package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.vertical_refresh_recycler.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ArticleTypeModel
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.repository.SearchResultRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.service.HistoryKeyServiceImpl
import wang.mycroft.lib.sample.service.IHistoryKeyService
import wang.mycroft.lib.sample.shared.SearchViewModel
import wang.mycroft.lib.sample.ui.activity.ArticleWebViewActivity
import wang.mycroft.lib.sample.ui.adapter.recycler.ArticleListAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import java.util.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class SearchResultFragment : CommonFragment() {

    companion object {
        private const val START_PAGE = 0
    }

    private val historyKeyService: IHistoryKeyService = HistoryKeyServiceImpl

    private val searchResultList = ArrayList<ArticleTypeModel>()

    private var adapter: ArticleListAdapter? = null

    private var key: String? = null

    private var requestingPage = START_PAGE

    private var currentPage = START_PAGE

    private lateinit var searchResultRepository: SearchResultRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProvider(activity!!)
        viewModelProvider[SearchViewModel::class.java]
            .searchKey
            .observe(this, searchObserver)

        searchResultRepository = viewModelProvider[SearchResultRepository::class.java]

        searchResultRepository.articleList.observe(this, observer)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.vertical_refresh_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)

        adapter = ArticleListAdapter(searchResultList)

        adapter!!.onItemClickListener = onItemClickListener

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onDestroyView() {
        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null

        super.onDestroyView()
    }

    private val searchObserver = Observer<String> { s ->
        key = s
        loadData(START_PAGE)
    }

    private val observer = Observer<ResultModel<ListData<Article>>> { resultModel ->
        finishRefresh()
        if (resultModel.errorCode != ResultModel.CODE_SUCCESS) {
            ToastUtils.showShort(resultModel.errorMsg)
        } else {
            val listData = resultModel.data
            currentPage = requestingPage

            if (requestingPage == START_PAGE) {
                searchResultList.clear()
                recyclerView.smoothScrollToPosition(0)
            }

            listData.datas.forEach {
                searchResultList.add(ArticleTypeModel(it))
            }

            adapter?.notifyDataSetChanged()
        }
    }

    private var isLoading = false

    private fun loadData(page: Int) {
        if (isLoading) {
            return
        }

        if (page == START_PAGE) {
            showLoadingDialog()
        }

        historyKeyService.addHistoryKey(key!!)

        requestingPage = page
        isLoading = true
        searchResultRepository.loadData(key!!, page)
    }

    private fun finishRefresh() {
        isLoading = false
        hideLoadingDialog()

        refreshLayout?.run {
            if (isRefreshing) {
                finishRefresh()
            }
            if (isLoading) {
                finishLoadMore()
            }
        }
    }

    private val refreshLoadMoreListener = object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            loadData(currentPage + 1)
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            loadData(START_PAGE)
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        startActivity(
            ArticleWebViewActivity.getIntent(
                context!!,
                searchResultList[position].article.title,
                searchResultList[position].article.link
            )
        )
    }
}