package wang.mycroft.lib.sample.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import wang.mycroft.lib.sample.R
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
    override fun getItem(position: Int): Fragment {
        return if (0 == position) {
            CategoryFragment.newInstance()
        } else {
            ToolsFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return StringUtils.getStringArray(R.array.tab_tool)[position]
    }
}