package wang.mycroft.lib.sample.ui.main.fragment

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
import wang.mycroft.lib.sample.model.Navigation
import wang.mycroft.lib.sample.repository.model.CODE_SUCCESS
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.main.adapter.NavigationAdapter
import wang.mycroft.lib.sample.viewmodel.NavigationViewModel
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder

/**
 * 导航页面
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class NavigationFragment : CommonFragment() {

    companion object {
        fun newInstance(): NavigationFragment =
            NavigationFragment()
    }

    private val navigationViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationViewModel.navigationListLiveData.observe(this, toolsListObserver)

        navigationViewModel.loadNavigationList()
    }

    private var holder: LoadingHolder? = null

    private var adapter =
        NavigationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        holder = Loading.getDefault().wrap(view).withRetry {
            holder!!.showLoading()
            navigationViewModel.loadNavigationList()
        }
        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onDestroyView() {
        holder = null
        super.onDestroyView()
    }

    private val toolsListObserver = Observer<ResultModel<List<Navigation>>> { resultModel ->
        if (resultModel.code == CODE_SUCCESS) {
            holder?.showLoadSuccess()
            resultModel.data?.let {
                adapter.navigationList = it
            }
        } else {
            holder?.showLoadFailed()
        }
    }

}