package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_category.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.ToolsHeader
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.ui.adapter.recycler.ToolsAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import wang.mycroft.lib.util.DisposableUtil
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

            val args = Bundle()

            val fragment = ToolsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var holder: LoadingHolder

    private var adapter: ToolsAdapter? = null

    private var disposable: Disposable? = null

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
        adapter = ToolsAdapter(ArrayList())
        recyclerView.run {
            this.adapter = this@ToolsFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        if (adapter!!.data.isEmpty()) {
            holder.showLoading()
        } else {
            holder.showLoadSuccess()
        }
    }

    private fun loadData() {
        if (disposable != null) {
            return
        }

        disposable = NetService.getInstance().toolList
            .subscribe({ toolsList ->
                holder.showLoadSuccess()
                for (item in toolsList) {
                    adapter!!.addData(ToolsHeader(item))
                }
            }, { throwable ->
                disposable = null
                holder.showLoadFailed()
                ToastUtils.showShort(throwable.message)
            }, { disposable = null })
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.data.isEmpty()) {
            loadData()
        }
    }

    override fun onDestroyView() {
        DisposableUtil.dispose(disposable)
        disposable = null

        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null
        super.onDestroyView()
    }

}