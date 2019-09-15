package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.donkingliang.labels.LabelsView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_hot_key.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.HotKey
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.shared.SearchViewModel
import java.util.*

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class HotKeyFragment : CommonFragment() {

    private lateinit var searchViewModel: SearchViewModel

    private val hotKeyList = ArrayList<HotKey>()

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(activity!!).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hot_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        labelsView.run {
            selectType = LabelsView.SelectType.NONE
            setOnLabelClickListener { _, _, position ->
                searchViewModel.setSearchKey(
                    hotKeyList[position].name
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hotKeyList.isEmpty()) {
            loadData()
        } else {
            showHotKey()
        }
    }

    private fun loadData() {
        if (disposable != null) {
            return
        }

        disposable = NetService.getInstance().hotKeyList
            .subscribe({ hotKeys ->
                hotKeyList.addAll(hotKeys)
                showHotKey()
            }, { throwable ->
                disposable = null
                ToastUtils.showShort(throwable.message)
            },
                { disposable = null })
    }

    private fun showHotKey() {
        hotKeyLabel.visibility = View.VISIBLE
        labelsView.setLabels(hotKeyList) { _, _, data -> data.name }
    }
}