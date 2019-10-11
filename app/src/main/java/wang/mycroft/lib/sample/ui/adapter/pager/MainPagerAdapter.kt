package wang.mycroft.lib.sample.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.ui.fragment.HomeFragment
import wang.mycroft.lib.sample.ui.fragment.OfficialAccountFragment
import wang.mycroft.lib.sample.ui.fragment.ProjectFragment
import wang.mycroft.lib.sample.ui.fragment.ToolFragment

/**
 * 主页 pager adapter
 *
 * @author Mycroft Wong
 * @date 2019年10月10日
 */
class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val tabArray: Array<String> = StringUtils.getStringArray(R.array.tab_main)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> ToolFragment.newInstance()
            2 -> OfficialAccountFragment.newInstance()
            3 -> ProjectFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return tabArray.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabArray[position]
    }
}