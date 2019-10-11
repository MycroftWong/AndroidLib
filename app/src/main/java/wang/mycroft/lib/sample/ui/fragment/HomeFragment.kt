package wang.mycroft.lib.sample.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjq.bar.OnTitleBarListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.titleBar
import kotlinx.android.synthetic.main.vertical_refresh_recycler.*
import wang.mycroft.lib.net.GlideApp
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ArticleTypeModel
import wang.mycroft.lib.sample.model.Banner
import wang.mycroft.lib.sample.model.ListData
import wang.mycroft.lib.sample.repository.HomeRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.activity.SearchActivity
import wang.mycroft.lib.sample.ui.activity.WebViewActivity
import wang.mycroft.lib.sample.ui.adapter.recycler.ArticleListAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月14
 * @author: wangqiang
 */
class HomeFragment : CommonFragment() {

    companion object {
        private const val STATE_NEXT_PAGE = "next_page.state"

        private const val STATE_ARTICLE_LIST = "article_list.state"

        private const val STATE_BANNER_LIST = "banner_list.state"

        private const val START_PAGE = 0

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val homeRepository by lazy {
        ViewModelProvider(this).get(HomeRepository::class.java)
    }

    private var nextPage = START_PAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            nextPage = it.getInt(STATE_NEXT_PAGE)
            it.getParcelableArrayList<ArticleTypeModel>(STATE_ARTICLE_LIST)
                ?.let { articleTypeModelList ->
                    articleTypeModels.addAll(articleTypeModelList)
                }

            it.getParcelableArrayList<Banner>(STATE_BANNER_LIST)?.let { bannerList ->
                this.bannerList.addAll(bannerList)
            }
        }

        homeRepository.articleList.observe(this, articleListObserver)
        homeRepository.bannerList.observe(this, bannerListObserver)
        homeRepository.topArticleList.observe(this, topArticleListObserver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_NEXT_PAGE, nextPage)
        outState.putParcelableArrayList(STATE_ARTICLE_LIST, articleTypeModels)
        outState.putParcelableArrayList(STATE_BANNER_LIST, bannerList)
    }

    private val articleTypeModels = ArrayList<ArticleTypeModel>()

    private var adapter: ArticleListAdapter? = null

    private var holder: LoadingHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        holder = Loading.getDefault().wrap(view).withRetry {
            holder?.showLoading()
            loadData(START_PAGE)
        }
        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {}

            override fun onTitleClick(v: View) {}

            override fun onRightClick(v: View) {
                startActivity(SearchActivity.getIntent(context!!))
            }
        })

        adapter = ArticleListAdapter(articleTypeModels)

        adapter!!.onItemClickListener = onItemClickListener

        with(recyclerView) {
            addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
            this.adapter = this@HomeFragment.adapter
        }

        if (bannerList.isNotEmpty()) {
            createBanner()
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

            if (page == START_PAGE) {
                homeRepository.loadBannerList()
                homeRepository.loadTopArticleList()
            }
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
        bannerLayout = null

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

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        startActivity(
            WebViewActivity.getIntent(
                context!!,
                articleTypeModels[position].article.title,
                articleTypeModels[position].article.link
            )
        )
    }

    private val articleListObserver =
        Observer<ResultModel<ListData<Article>>> { listDataResultModel ->
            if (listDataResultModel.errorCode != ResultModel.CODE_SUCCESS) {
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
        }

    private val bannerList = ArrayList<Banner>()

    private val bannerListObserver = Observer<ResultModel<List<Banner>>> { resultModel ->
        if (resultModel.errorCode == ResultModel.CODE_SUCCESS) {
            bannerList.clear()
            bannerList.addAll(resultModel.data)
            if (adapter!!.headerLayoutCount == 0) {
                createBanner()
            } else {
                bannerLayout?.update(resultModel.data)
            }
        }
    }

    private val topArticleListObserver = Observer<ResultModel<List<Article>>> { resultModel ->
        if (resultModel.errorCode == ResultModel.CODE_SUCCESS) {
            articleTypeModels.removeAll { it.article.type == 1 }

            resultModel.data.map { ArticleTypeModel(it) }.reversed()
                .forEach { articleTypeModels.add(0, it) }

            adapter?.notifyDataSetChanged()
        }
    }

    private var bannerLayout: com.youth.banner.Banner? = null

    private fun createBanner() {
        val view = layoutInflater.inflate(R.layout.layout_home_banner, recyclerView, false)
        bannerLayout = view.findViewById(R.id.bannerLayout)

        with(bannerLayout!!) {
            val screenWidth = ScreenUtils.getScreenWidth()
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                screenWidth * 9 / 16
            )
            setImages(bannerList)
            isAutoPlay(true)
            setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context?, path: Any?, imageView: ImageView) {
                    val url = path as Banner?
                    GlideApp.with(imageView)
                        .load(url?.imagePath)
                        .into(imageView)
                }
            })

            setOnBannerListener {
            }

            start()
        }

        adapter?.setHeaderView(view)
    }

}