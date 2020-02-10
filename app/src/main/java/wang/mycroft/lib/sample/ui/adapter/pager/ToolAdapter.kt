package wang.mycroft.lib.sample.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.ui.fragment.ArticleListFragment
import wang.mycroft.lib.sample.ui.fragment.CategoryFragment
import wang.mycroft.lib.sample.ui.fragment.ToolsFragment

/**
 * 体系 pager adapter
 *
 * @author Mycroft Wong
 * @date 2019年10月11日
 */
class ToolAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleArray = StringUtils.getStringArray(R.array.tab_tool)

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> CategoryFragment.newInstance()
        1 -> ToolsFragment.newInstance()
        else -> ArticleListFragment.newInstance("user_article/list/%d/json", 0)
    }

    override fun getCount(): Int = titleArray.size

    override fun getPageTitle(position: Int): CharSequence? = titleArray[position]
}