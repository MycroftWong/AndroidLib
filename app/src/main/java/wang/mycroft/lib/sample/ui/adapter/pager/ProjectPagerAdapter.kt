package wang.mycroft.lib.sample.ui.adapter.pager

import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import wang.mycroft.lib.sample.model.Project
import wang.mycroft.lib.sample.ui.fragment.ArticleListFragment

/**
 * 项目分类
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class ProjectPagerAdapter(fm: FragmentManager, private val projectList: List<Project>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return ArticleListFragment.newInstance(
            "project/list/%d/json?cid=" + projectList[position].id,
            1
        )
    }

    override fun getCount(): Int {
        return projectList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return HtmlCompat.fromHtml(projectList[position].name, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
}