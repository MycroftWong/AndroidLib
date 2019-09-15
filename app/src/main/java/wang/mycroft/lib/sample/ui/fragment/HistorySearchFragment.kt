package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.LogUtils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_history_search.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.HistoryKey
import wang.mycroft.lib.sample.service.HistoryKeyServiceImpl
import wang.mycroft.lib.sample.service.IHistoryKeyService
import wang.mycroft.lib.sample.shared.SearchViewModel
import wang.mycroft.lib.sample.ui.adapter.recycler.HistorySearchAdapter
import wang.mycroft.lib.util.BaseQuickAdapterUtil
import wang.mycroft.lib.util.DisposableUtil
import java.util.*

/**
 * 历史搜索
 * 1. 加载热门搜索关键词
 * 2. 操作搜索历史记录
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class HistorySearchFragment : CommonFragment() {

    private lateinit var searchViewModel: SearchViewModel

    private val historyKeyService: IHistoryKeyService = HistoryKeyServiceImpl

    private val historySearchKey = ArrayList<HistoryKey>()

    private var adapter: HistorySearchAdapter? = null

    private var disposable: Disposable? = null

    private val clearClickListener = View.OnClickListener {
        historySearchKey.clear()
        adapter!!.notifyDataSetChanged()
        historyKeyService.clearHistoryKey()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(activity!!).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HistorySearchAdapter(historySearchKey)

        adapter?.run {
            addHeaderView(createHeaderView(LayoutInflater.from(context), recyclerView))
            setOnItemClickListener { _, _, position ->
                searchViewModel.setSearchKey(historySearchKey[position].key)
            }
            setOnItemChildClickListener { _, _, position ->
                val historyKey = historySearchKey.removeAt(position)
                historyKeyService.deleteHistoryKey(historyKey)
                adapter?.notifyDataSetChanged()
            }
        }

        recyclerView.run {
            this.adapter = this@HistorySearchFragment.adapter
            val itemDecoration = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.line)!!)
            addItemDecoration(itemDecoration)
        }
    }

    override fun onResume() {
        super.onResume()
        disposable = historyKeyService
            .allHistoryKey
            .subscribe({ historyKeys ->
                historySearchKey.clear()
                historySearchKey.addAll(historyKeys)
                adapter!!.notifyDataSetChanged()
            }, { LogUtils.e(it) },
                { disposable = null })
    }

    override fun onDestroyView() {
        DisposableUtil.dispose(disposable)
        disposable = null
        BaseQuickAdapterUtil.releaseAdapter(adapter)
        adapter = null
        super.onDestroyView()
    }

    private fun createHeaderView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.label_search_history, container, false)
        val clearAllText = view.findViewById<TextView>(R.id.clearAllText)
        clearAllText.setOnClickListener(clearClickListener)
        return view
    }
}