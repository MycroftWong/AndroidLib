package wang.mycroft.lib.sample.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.ui.fragment.ArticleListFragment

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class CategoryDetailPagerAdapter(fm: FragmentManager, private val category: Category) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return ArticleListFragment.newInstance(
            "article/list/%d/json?cid=" + category.children[position].id,
            0
        )
    }

    override fun getCount(): Int {
        return category.children.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return category.children[position].name
    }
}
