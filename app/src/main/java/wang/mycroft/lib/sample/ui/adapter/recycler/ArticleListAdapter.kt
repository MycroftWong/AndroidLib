package wang.mycroft.lib.sample.ui.adapter.recycler

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.youth.banner.Banner
import wang.mycroft.lib.net.GlideApp
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.Article
import wang.mycroft.lib.sample.model.ArticleTypeModel
import wang.mycroft.lib.sample.ui.activity.ArticleWebViewActivity

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

class ArticleListAdapter2 : RecyclerView.Adapter<ArticleListAdapter2.ViewHolder>() {

    var articleList: List<Article>? = null
        set(value) {
            if (field == null) {
                field = value
                notifyItemRangeInserted(0, value!!.size)
            } else {
                val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun areItemsTheSame(
                        oldItemPosition: Int, newItemPosition: Int
                    ): Boolean {
                        return field!![oldItemPosition].id == value!![newItemPosition].id
                    }

                    override fun getOldListSize(): Int = field!!.size

                    override fun getNewListSize(): Int = value!!.size

                    override fun areContentsTheSame(
                        oldItemPosition: Int, newItemPosition: Int
                    ): Boolean {
                        val oldItem = field!![oldItemPosition]
                        val newItem = value!![newItemPosition]
                        return oldItem.id == newItem.id
                    }
                })
                field = value;
                diffResult.dispatchUpdatesTo(this)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_list_image, parent, false)
        ).apply {
            itemView.setOnClickListener {
                val context = it.context
                val article = articleList!![adapterPosition]
                context.startActivity(
                    ArticleWebViewActivity.getIntent(context, article.title, article.link)
                )
            }
        }

    override fun getItemCount(): Int = articleList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = articleList!![position]

        if (!item.envelopePic.isNullOrEmpty()) {
            holder.imageView.visibility = View.VISIBLE
            GlideApp.with(holder.imageView)
                .load(item.envelopePic)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView)
        } else {
            holder.imageView.visibility = View.GONE
        }

        holder.titleText.text =
            HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
        holder.chapterText.text = buildContent(item)
        holder.dateText.text = if (item.niceDate.isNullOrEmpty()) {
            item.niceShareDate
        } else {
            item.niceDate
        }
        holder.authorText.text = if (item.author.isNullOrEmpty()) {
            item.shareUser
        } else {
            item.author
        }

        when {
            item.type == 1 -> {
                holder.labelText.visibility = View.VISIBLE
                holder.labelText.text = StringUtils.getString(R.string.text_label_top)
            }
            item.isFresh -> {
                holder.labelText.visibility = View.VISIBLE
                holder.labelText.text = StringUtils.getString(R.string.text_label_fresh)
            }
            else -> {
                holder.labelText.visibility = View.GONE
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

    /**
     * 文章列表[ViewHolder]
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val chapterText: TextView = itemView.findViewById(R.id.chapterText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val authorText: TextView = itemView.findViewById(R.id.authorText)
        val labelText: TextView = itemView.findViewById(R.id.labelText)
    }

    class BannerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_home_banner, parent, false)
    ) {
        val bannerLayout: Banner = itemView.findViewById(R.id.bannerLayout)
    }
}
