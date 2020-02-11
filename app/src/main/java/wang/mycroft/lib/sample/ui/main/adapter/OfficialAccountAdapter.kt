package wang.mycroft.lib.sample.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import wang.mycroft.lib.sample.model.OfficialAccount
import wang.mycroft.lib.sample.ui.article.ArticleListFragment

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class OfficialAccountAdapter(
    fragmentManager: FragmentManager,
    private val officialAccountList: List<OfficialAccount>
) :
    FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int): Fragment {
        return ArticleListFragment.newInstance(
            "wxarticle/list/" + officialAccountList[position].id + "/%d/json",
            1
        )
    }

    override fun getCount(): Int {
        return officialAccountList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return officialAccountList[position].name
    }
}
