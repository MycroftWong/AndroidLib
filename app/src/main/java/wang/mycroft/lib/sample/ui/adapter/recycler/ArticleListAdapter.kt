package wang.mycroft.lib.sample.ui.adapter.recycler

import android.text.TextUtils
import android.widget.ImageView
import androidx.core.text.HtmlCompat
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
class ArticleListAdapter(data: List<ArticleTypeModel>) :
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
            .setText(R.id.authorText, item.author)
            .setText(R.id.dateText, item.niceDate)
    }

    private val builder = StringBuilder()

    private fun buildContent(result: Article): String {
        builder.setLength(0)

        val hasSuperChapter = !TextUtils.isEmpty(result.superChapterName)
        val hasChapter = !TextUtils.isEmpty(result.chapterName)

        return if (hasSuperChapter && hasChapter) {
            builder.append(result.superChapterName).append("·").append(result.chapterName)
                .toString()
        } else if (!hasSuperChapter && !hasChapter) {
            ""
        } else {
            if (result.superChapterName == null) result.chapterName else result.superChapterName
        }
    }
}