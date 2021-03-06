package wang.mycroft.lib.sample.ui.search

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import wang.mycroft.lib.net.GlideApp
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ArticleTypeModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class SearchResultArticleListAdapter(data: List<ArticleTypeModel>) :
    BaseMultiItemQuickAdapter<ArticleTypeModel, BaseViewHolder>(data) {

    init {
        addItemType(ArticleTypeModel.TYPE_TEXT, R.layout.item_article_list_text)
        addItemType(ArticleTypeModel.TYPE_IMAGE, R.layout.item_article_list_image)
    }

    override fun convert(helper: BaseViewHolder, articleTypeModel: ArticleTypeModel) {
        val item = articleTypeModel.article

        if (ArticleTypeModel.TYPE_IMAGE == articleTypeModel.itemType) {
            val imageView = helper.getView<ImageView>(R.id.imageView)
            GlideApp.with(imageView)
                .load(item.envelopePic)
                .placeholder(R.drawable.placeholder)
                .into(imageView)
        }

        helper.setText(
            R.id.titleText,
            HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
        )
            .setText(R.id.chapterText, buildContent(item))

        val dateText = helper.getView<TextView>(R.id.dateText)
        dateText.text = if (TextUtils.isEmpty(item.niceDate)) {
            item.niceShareDate
        } else {
            item.niceDate
        }

        val authorText = helper.getView<TextView>(R.id.authorText)
        authorText.text = if (TextUtils.isEmpty(item.author)) {
            item.shareUser
        } else {
            item.author
        }

        val labelText = helper.getView<TextView>(R.id.labelText)
        when {
            item.type == 1 -> {
                labelText.visibility = View.VISIBLE
                labelText.text = StringUtils.getString(R.string.text_label_top)
            }
            item.isFresh -> {
                labelText.visibility = View.VISIBLE
                labelText.text = StringUtils.getString(R.string.text_label_fresh)
            }
            else -> {
                labelText.visibility = View.GONE
            }
        }
    }

    private val builder = StringBuilder()

    private fun buildContent(result: Article): String {
        builder.setLength(0)

        val hasSuperChapter = !TextUtils.isEmpty(result.superChapterName)
        val hasChapter = !TextUtils.isEmpty(result.chapterName)

        return if (hasSuperChapter && hasChapter) {
            builder.append(
                HtmlCompat.fromHtml(result.superChapterName, HtmlCompat.FROM_HTML_MODE_COMPACT)
            )
                .append("\u3000")
                .append(HtmlCompat.fromHtml(result.chapterName, HtmlCompat.FROM_HTML_MODE_COMPACT))
                .toString()
        } else if (!hasSuperChapter && !hasChapter) {
            ""
        } else {
            if (result.superChapterName == null)
                HtmlCompat.fromHtml(result.chapterName, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    .toString()
            else
                HtmlCompat.fromHtml(result.superChapterName, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    .toString()
        }
    }
}