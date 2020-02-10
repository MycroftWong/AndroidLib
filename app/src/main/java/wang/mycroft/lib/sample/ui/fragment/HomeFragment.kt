package wang.mycroft.lib.sample.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*
import wang.mycroft.lib.net.GlideApp
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.Banner
import wang.mycroft.lib.sample.repository.HomeViewModel
import wang.mycroft.lib.sample.repository.model.CODE_SUCCESS
import wang.mycroft.lib.sample.repository.model.PagedResultModel
import wang.mycroft.lib.sample.ui.activity.ArticleWebViewActivity
import wang.mycroft.lib.sample.ui.activity.SearchHistoryActivity
import wang.mycroft.lib.sample.ui.adapter.recycler.ArticleListAdapter2
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
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        homeViewModel.articleListLiveData.observe(this, articleListObserver)
        homeViewModel.bannerLiveData.observe(this, bannerListObserver)
    }

    private val adapter = ArticleListAdapter2()

    private var holder: LoadingHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        holder = Loading.getDefault().wrap(view).withRetry {
            homeViewModel.loadArticleList(true)
        }
        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setTitle(R.string.main_page)

        recyclerView.addItemDecoration(
            DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        )
        recyclerView.adapter = adapter

        bannerLayout.visibility = if (bannerList.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        bannerLayout.isAutoPlay(true)

        bannerLayout.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView) {
                val banner = path as Banner?
                GlideApp.with(imageView)
                    .load(banner?.imagePath)
                    .into(imageView)
            }
        })
        bannerLayout.setImages(bannerList)
        bannerLayout.setOnBannerListener {
            startActivity(
                ArticleWebViewActivity.getIntent(
                    requireContext(), bannerList[it].title, bannerList[it].url
                )
            )
        }

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_fragment_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.searchAction -> {
            startActivity(SearchHistoryActivity.getIntent(context!!))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        bannerLayout.startAutoPlay()
        if (adapter.articleList.isNullOrEmpty()) {
            homeViewModel.loadArticleList(true)
        }
    }

    override fun onPause() {
        super.onPause()
        bannerLayout.stopAutoPlay()
    }

    private fun finishRefresh() {
        with(refreshLayout) {
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
            homeViewModel.loadArticleList(false)
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            homeViewModel.loadArticleList(true)
        }
    }

    private val articleListObserver =
        Observer<PagedResultModel<out List<Article>>> { resultModel ->
            if (resultModel.code != CODE_SUCCESS) {
                if (adapter.articleList.isNullOrEmpty()) {
                    holder?.showLoadFailed()
                } else {
                    ToastUtils.showShort(resultModel.message)
                }
            } else {
                holder?.showLoadSuccess()
                val pagedResult = resultModel.data!!

                refreshLayout.setNoMoreData(pagedResult.noMoreData)
                adapter.articleList = pagedResult.data
            }
            finishRefresh()
        }

    private val bannerList = ArrayList<Banner>()

    private val bannerListObserver = Observer<List<Banner>> { bannerList ->
        this.bannerList.addAll(bannerList)

        if (bannerList.isNullOrEmpty()) {
            bannerLayout.visibility = View.GONE
        } else {
            bannerLayout.visibility = View.VISIBLE
            bannerLayout.setImages(this.bannerList)
            bannerLayout.start()
        }
    }
}