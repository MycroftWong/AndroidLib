package wang.mycroft.lib.sample.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.KeyboardUtils
import com.donkingliang.labels.LabelsView
import kotlinx.android.synthetic.main.activity_search_history.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.model.HistoryKey
import wang.mycroft.lib.sample.model.HotKey
import wang.mycroft.lib.sample.repository.HistorySearchRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.service.HistoryKeyServiceImpl
import wang.mycroft.lib.sample.service.IHistoryKeyService
import wang.mycroft.lib.sample.ui.adapter.recycler.HistorySearchAdapter

/**
 * 搜索历史页面
 *
 * @blog: https://blog.mycroft.wang
 * @author Mycroft Wong
 * @date 2019年12月17日
 */
class SearchHistoryActivity : CommonActivity() {

    companion object {
        private const val REQUEST_SEARCH = 0x111

        fun getIntent(content: Context): Intent {
            return Intent(content, SearchHistoryActivity::class.java)
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_search_history
    }

    private val historyKeyService: IHistoryKeyService = HistoryKeyServiceImpl

    private val historySearchKey = mutableListOf<HistoryKey>()

    private val hotKeyList = mutableListOf<HotKey>()

    private val adapter: HistorySearchAdapter by lazy {
        HistorySearchAdapter(historySearchKey)
    }

    private val historySearchRepository: HistorySearchRepository by viewModels()

    override fun initFields(savedInstanceState: Bundle?) {

        historySearchRepository.hotKeyList.observe(this, hotKeyObserver)
        historySearchRepository.historySearchList.observe(this, historySearchListObserver)
    }

    override fun initViews() {

        searchButton.setOnClickListener {
            if (searchEdit.text.trim().isEmpty()) {
                finish()
            } else {
                doSearch(searchEdit.text!!.toString())
            }
        }

        searchEdit.doAfterTextChanged {
            if (searchEdit.text.trim().isNotEmpty()) {
                clearImage.visibility = View.VISIBLE
                searchButton.setText(R.string.search)
            } else {
                clearImage.visibility = View.GONE
                searchButton.setText(R.string.cancel)
            }
        }

        searchEdit.requestFocus()

        clearImage.setOnClickListener { searchEdit.text = null }

        searchEdit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && v.text.trim().isNotEmpty()) {
                doSearch(v.text.trim().toString())
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        with(adapter) {
            addHeaderView(
                createHeaderView(
                    LayoutInflater.from(this@SearchHistoryActivity), recyclerView
                )
            )
            setOnItemClickListener { _, _, position -> doSearch(historySearchKey[position].key) }
            setOnItemChildClickListener { _, _, position ->
                val historyKey = historySearchKey.removeAt(position)
                historyKeyService.deleteHistoryKey(historyKey)
                adapter.notifyDataSetChanged()
            }
        }

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this@SearchHistoryActivity, DividerItemDecoration.VERTICAL)
        )

        with(labelsView) {
            selectType = LabelsView.SelectType.NONE
            setOnLabelClickListener { _, _, position -> doSearch(hotKeyList[position].name) }
            labelsView.setLabels(hotKeyList) { _, _, data -> data.name }
        }
    }

    override fun loadData() {
        historySearchRepository.loadHotKeyList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SEARCH) {
            when (resultCode) {
                Activity.RESULT_OK -> finish()
                Activity.RESULT_CANCELED -> {
                    searchEdit.requestFocus()
                    KeyboardUtils.showSoftInput(searchEdit, InputMethodManager.SHOW_FORCED)
                }
            }
        }
    }

    private fun doSearch(keyword: String) {
        historyKeyService.addHistoryKey(keyword)
        startActivityForResult(SearchActivity.getIntent(this, keyword), REQUEST_SEARCH)
        overridePendingTransition(0, 0)
    }

    private val hotKeyObserver = Observer<ResultModel<List<HotKey>>> { resultModel ->
        if (resultModel.data.isNullOrEmpty()) {
            hotKeyContainer.visibility = View.GONE
        } else {
            hotKeyList.clear()
            hotKeyList.addAll(resultModel.data)
            hotKeyContainer.visibility = View.VISIBLE
            labelsView.setLabels(hotKeyList) { _, _, data -> data.name }
        }
    }

    private val historySearchListObserver = Observer<List<HistoryKey>> {
        if (it.isNotEmpty()) {
            historySearchKey.clear()
            historySearchKey.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun createHeaderView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.label_search_history, container, false)
        val clearAllText = view.findViewById<TextView>(R.id.clearAllText)
        clearAllText.setOnClickListener(clearClickListener)
        return view
    }

    private val clearClickListener = View.OnClickListener {
        historySearchKey.clear()
        adapter.notifyDataSetChanged()
        historyKeyService.clearHistoryKey()
    }
}
