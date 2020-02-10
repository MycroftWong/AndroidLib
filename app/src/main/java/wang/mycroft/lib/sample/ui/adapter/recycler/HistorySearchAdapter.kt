package wang.mycroft.lib.sample.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

class HistorySearchAdapter2(
    private val itemClickListener: (position: Int) -> Unit,
    private val deleteClickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<HistorySearchViewHolder>() {
    var data: MutableList<HistoryKey>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistorySearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history_key, parent, false)
        ).apply {
            itemView.setOnClickListener { itemClickListener(adapterPosition) }
            closeImage.setOnClickListener { deleteClickListener(adapterPosition) }
        }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: HistorySearchViewHolder, position: Int) {
        holder.titleText.text = data!![position].key
    }
}

class HistorySearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleText: TextView = itemView.findViewById(R.id.titleText)
    val closeImage: ImageView = itemView.findViewById(R.id.closeImage)
}