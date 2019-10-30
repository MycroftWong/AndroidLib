package wang.mycroft.lib.sample.ui.adapter.recycler

import android.app.Activity
import android.app.ActivityOptions
import android.util.Pair
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.ToolsContent
import wang.mycroft.lib.sample.model.ToolsHeader
import wang.mycroft.lib.sample.ui.activity.ArticleWebViewActivity

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15日
 * @author: wangqiang
 */
class ToolsAdapter(data: List<MultiItemEntity>?) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    companion object {

        const val TYPE_HEADER = 1

        const val TYPE_CONTENT = 2
    }

    init {
        addItemType(TYPE_HEADER, android.R.layout.simple_list_item_1)
        addItemType(TYPE_CONTENT, android.R.layout.simple_expandable_list_item_1)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            TYPE_HEADER -> {
                val header = item as ToolsHeader
                helper.setText(android.R.id.text1, header.tools.name)
                helper.itemView.setOnClickListener {
                    val pos = helper.adapterPosition
                    if (header.isExpanded) {
                        collapse(pos)
                    } else {
                        expand(pos)
                    }
                }
            }
            TYPE_CONTENT -> {
                val content = item as ToolsContent
                helper.setText(android.R.id.text1, content.article.title)
                helper.itemView.setOnClickListener {
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        helper.itemView.context as Activity, Pair(
                            it, StringUtils.getString(R.string.transition_content)
                        )
                    )
                    helper.itemView.context
                        .startActivity(
                            ArticleWebViewActivity.getIntent(
                                helper.itemView.context,
                                content.article.title,
                                content.article.link
                            ),options.toBundle()
                        )
                }
            }
        }
    }

}