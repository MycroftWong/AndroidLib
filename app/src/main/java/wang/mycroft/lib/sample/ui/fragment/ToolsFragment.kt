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
import wang.mycroft.lib.sample.model.Tools
import wang.mycroft.lib.sample.model.ToolsHeader
import wang.mycroft.lib.sample.repository.ToolsRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.adapter.recycler.ToolsAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
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

        fun newInstance(): ToolsFragment {
            return ToolsFragment()
        }
    }

    private val toolsRepository: ToolsRepository by lazy {
        ViewModelProvider(this)[ToolsRepository::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolsRepository.toolsList.observe(this, toolsListObserver)
    }

    private var holder: LoadingHolder? = null

    private var adapter: ToolsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        holder = Loading.getDefault().wrap(view).withRetry {
            holder!!.showLoading()
            toolsRepository.loadToolsList()
        }
        return holder!!.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ToolsAdapter(ArrayList())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
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
            toolsRepository.loadToolsList()
        }
    }

    override fun onDestroyView() {
        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null
        super.onDestroyView()
    }

    private val toolsListObserver = Observer<ResultModel<List<Tools>>> { resultModel ->
        if (resultModel.errorCode == ResultModel.CODE_SUCCESS) {
            holder?.showLoadSuccess()
            for (item in resultModel.data) {
                adapter!!.addData(ToolsHeader(item))
            }
        } else {
            holder?.showLoadFailed()
        }
    }

}