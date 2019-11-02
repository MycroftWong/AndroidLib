package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_tool.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.ui.adapter.pager.ToolAdapter
import wang.mycroft.lib.sample.ui.view.OnTabSelectedAdapter

/**
 * 体系
 *
 * @author Mycroft Wong
 * @date 2019年10月11日
 */
class ToolFragment : CommonFragment() {

    companion object {
        fun newInstance(): ToolFragment {
            return ToolFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tool, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = ToolAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedAdapter() {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.position, false)
            }
        })
    }
}