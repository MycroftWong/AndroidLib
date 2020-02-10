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
import wang.mycroft.lib.sample.model.Tools
import wang.mycroft.lib.sample.model.ToolsHeader
import wang.mycroft.lib.sample.repository.model.CODE_SUCCESS
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.adapter.recycler.ToolsAdapter
import wang.mycroft.lib.sample.ui.util.BaseQuickAdapterUtil
import wang.mycroft.lib.sample.viewmodel.ToolsViewModel
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder
import java.util.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class ToolsFragment : CommonFragment() {

    companion object {
        fun newInstance(): ToolsFragment = ToolsFragment()
    }

    private val toolsViewModel: ToolsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolsViewModel.toolsList.observe(this, toolsListObserver)
    }

    private var holder: LoadingHolder? = null

    private var adapter: ToolsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        holder = Loading.getDefault().wrap(view).withRetry {
            holder!!.showLoading()
            toolsViewModel.loadToolsList()
        }
        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ToolsAdapter(ArrayList())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        if (adapter!!.data.isEmpty()) {
            holder?.showLoading()
        } else {
            holder?.showLoadSuccess()
        }
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.data.isEmpty()) {
            toolsViewModel.loadToolsList()
        }
    }

    override fun onDestroyView() {
        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null
        super.onDestroyView()
    }

    private val toolsListObserver = Observer<ResultModel<List<Tools>>> { resultModel ->
        if (resultModel.code == CODE_SUCCESS) {
            holder?.showLoadSuccess()
            resultModel.data?.forEach {
                adapter!!.addData(ToolsHeader(it))
            }
        } else {
            holder?.showLoadFailed()
        }
    }

}