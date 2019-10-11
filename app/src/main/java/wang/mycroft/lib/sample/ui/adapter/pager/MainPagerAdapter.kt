package wang.mycroft.lib.sample.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.ui.fragment.*

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
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> CategoryFragment.newInstance()
            2 -> OfficialAccountFragment.newInstance()
            3 -> ToolsFragment.newInstance()
            4 -> ProjectFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> StringUtils.getString(R.string.main_page)
            1 -> StringUtils.getString(R.string.tools)
            2 -> StringUtils.getString(R.string.official_account)
            3 -> StringUtils.getString(R.string.tool)
            4 -> StringUtils.getString(R.string.project)
            else -> StringUtils.getString(R.string.main_page)
        }
    }
}