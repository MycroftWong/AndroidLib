package wang.mycroft.lib.sample.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.ui.main.fragment.HomeFragment
import wang.mycroft.lib.sample.ui.main.fragment.OfficialAccountFragment
import wang.mycroft.lib.sample.ui.main.fragment.ProjectFragment
import wang.mycroft.lib.sample.ui.main.fragment.ToolFragment

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
            2 -> ProjectFragment.newInstance()
            3 -> OfficialAccountFragment.newInstance()
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