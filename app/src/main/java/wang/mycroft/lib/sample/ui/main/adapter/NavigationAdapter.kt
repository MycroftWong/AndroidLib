package wang.mycroft.lib.sample.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.donkingliang.labels.LabelsView
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.Navigation
import wang.mycroft.lib.sample.ui.web.ArticleWebViewActivity

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15日
 * @author: wangqiang
 */
class NavigationAdapter : RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
    ) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val labelsView: LabelsView = itemView.findViewById(R.id.labelsView)
    }

    var navigationList: List<Navigation>? = null
        set(value) {
            if (field == null) {
                field = value
                notifyItemRangeInserted(0, value!!.size)
            } else {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent).apply {
        labelsView.setOnLabelClickListener { _, _, position ->
            val context = itemView.context
            val article = navigationList!![adapterPosition].articles[position]
            context.startActivity(
                ArticleWebViewActivity.getIntent(context, article.title, article.link)
            )
        }
    }

    override fun getItemCount(): Int = navigationList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val navigation = navigationList!![position]
        holder.titleText.text = navigation.name
        holder.labelsView.setLabels(navigation.articles) { _, _, data ->
            HtmlCompat.fromHtml(data.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }
}