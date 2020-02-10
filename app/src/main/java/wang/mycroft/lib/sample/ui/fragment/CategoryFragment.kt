package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_category.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.repository.CategoryViewModel
import wang.mycroft.lib.sample.repository.model.CODE_SUCCESS
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.adapter.recycler.CategoryAdapter
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
        fun newInstance(): CategoryFragment = CategoryFragment()
    }

    private val categoryList = ArrayList<Category>()

    private var holder: LoadingHolder? = null

    private var adapter = CategoryAdapter()

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel.categoryList.observe(this, categoryListObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        holder = Loading.getDefault().wrap(view).withRetry {
            holder?.showLoading()
            loadData()
        }

        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        recyclerView.adapter = adapter

        if (categoryList.isEmpty()) {
            holder?.showLoading()
        } else {
            holder?.showLoadSuccess()
        }
    }

    private var isLoading = false

    private fun loadData() {
        if (isLoading) {
            return
        }
        isLoading = true
        categoryViewModel.loadData()
    }

    override fun onResume() {
        super.onResume()
        if (adapter.categoryList.isNullOrEmpty()) {
            loadData()
        }
    }

    override fun onDestroyView() {
        holder = null
        super.onDestroyView()
    }

    private val categoryListObserver = Observer<ResultModel<List<Category>>> { resultModel ->
        if (resultModel.code != CODE_SUCCESS) {
            holder?.showLoadFailed()
        } else {
            holder?.showLoadSuccess()
            adapter.categoryList = resultModel.data
        }
        isLoading = false
    }

}