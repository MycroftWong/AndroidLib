package wang.mycroft.lib.sample.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.donkingliang.labels.LabelsView
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.ui.activity.CategoryDetailActivity

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var categoryList: List<Category>? = null
        set(value) {

            if (field == null) {
                field = value
                notifyItemRangeInserted(0, value!!.size)
            } else {
                val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun areItemsTheSame(
                        oldItemPosition: Int, newItemPosition: Int
                    ): Boolean = field!![oldItemPosition] == value!![newItemPosition]

                    override fun getOldListSize(): Int = field?.size ?: 0

                    override fun getNewListSize(): Int = value?.size ?: 0

                    override fun areContentsTheSame(
                        oldItemPosition: Int, newItemPosition: Int
                    ): Boolean = field!![oldItemPosition] == value!![newItemPosition]

                })
                diffResult.dispatchUpdatesTo(this)
                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
    ).apply {
        itemView.setOnClickListener {
            it.context.startActivity(
                CategoryDetailActivity.getIntent(it.context, categoryList!![adapterPosition])
            )
        }
    }

    override fun getItemCount(): Int = categoryList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList!![position]

        holder.titleText.text = category.name
        holder.labelsView.setLabels(category.children) { _, _, data ->
            HtmlCompat.fromHtml(data.name, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val labelsView: LabelsView = itemView.findViewById(R.id.labelsView)
    }
}

