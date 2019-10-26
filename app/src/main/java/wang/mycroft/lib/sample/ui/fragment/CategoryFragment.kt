package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_category.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.repository.CategoryRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.activity.CategoryDetailActivity
import wang.mycroft.lib.sample.ui.adapter.recycler.CategoryAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class CategoryFragment : CommonFragment() {

    companion object {

        private const val STATE_CATEGORY_LIST = "category_list.state"

        fun newInstance(): CategoryFragment {
            val args = Bundle()
            val fragment = CategoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val categoryList = ArrayList<Category>()

    private lateinit var holder: LoadingHolder
    private var adapter: CategoryAdapter? = null

    private val categoryRepository: CategoryRepository by lazy {
        ViewModelProvider(this)[CategoryRepository::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CATEGORY_LIST)) {
            val categories =
                savedInstanceState.getParcelableArrayList<Category>(STATE_CATEGORY_LIST)!!
            categoryList.addAll(categories)
        }

        categoryRepository.categoryList.observe(
            this,
            Observer<ResultModel<List<Category>>> { resultModel ->
                if (resultModel.errorCode != ResultModel.CODE_SUCCESS) {
                    holder.showLoadFailed()
                } else {
                    holder.showLoadSuccess()
                    categoryList.addAll(resultModel.data)
                    adapter?.notifyDataSetChanged()
                }
                isLoading = false
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_CATEGORY_LIST, categoryList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        holder = Loading.getDefault().wrap(view).withRetry {
            holder.showLoading()
            loadData()
        }

        return holder.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoryAdapter(categoryList)

        adapter!!.setOnItemClickListener { _, _, position ->
            startActivity(
                CategoryDetailActivity.getIntent(
                    context!!, categoryList[position]
                )
            )
        }

        recyclerView.run {
            this.adapter = this@CategoryFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        recyclerView.adapter = adapter

        if (categoryList.isEmpty()) {
            holder.showLoading()
        } else {
            holder.showLoadSuccess()
        }
    }

    private var isLoading = false

    private fun loadData() {
        if (isLoading) {
            return
        }
        isLoading = true
        categoryRepository.loadData()
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.data.isEmpty()) {
            loadData()
        }
    }

    override fun onDestroyView() {
        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null
        super.onDestroyView()
    }

}