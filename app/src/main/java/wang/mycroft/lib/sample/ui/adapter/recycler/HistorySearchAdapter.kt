package wang.mycroft.lib.sample.ui.adapter.recycler

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.HistoryKey

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class HistorySearchAdapter(data: List<HistoryKey>?) :
    BaseQuickAdapter<HistoryKey, BaseViewHolder>(R.layout.item_history_key, data) {

    override fun convert(helper: BaseViewHolder, item: HistoryKey) {
        helper.setText(R.id.titleText, item.key)
            .addOnClickListener(R.id.closeImage)
    }
}