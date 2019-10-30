package wang.mycroft.lib.sample.ui.adapter.recycler

import androidx.core.text.HtmlCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.donkingliang.labels.LabelsView
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.Category

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class CategoryAdapter(data: List<Category>) :
    BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_category, data) {

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_CONTENT = 2
    }

    override fun convert(helper: BaseViewHolder, item: Category) {
        helper.setText(R.id.titleText, item.name)

        val labelsView = helper.getView<LabelsView>(R.id.labelsView)
        labelsView.setLabels(item.children) { _, _, data ->
            HtmlCompat.fromHtml(data.name, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }

}