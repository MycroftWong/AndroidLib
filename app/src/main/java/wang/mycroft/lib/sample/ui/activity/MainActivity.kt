package wang.mycroft.lib.sample.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import wang.mycroft.lib.net.GlideApp
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.ui.adapter.pager.MainPagerAdapter

/**
 * 首页，使用了Navigation
 *
 * @blog: https://blog.mycroft.wang
 * @author wangqiang
 * @date 2019年9月13日
 */
class MainActivity : CommonActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_main
    }

    override fun initFields(savedInstanceState: Bundle?) {
    }

    override fun initViews() {

        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                navView.selectedItemId = when (position) {
                    0 -> R.id.navigation_home
                    1 -> R.id.navigation_tool
                    2 -> R.id.navigation_official
                    3 -> R.id.navigation_project
                    else -> R.id.navigation_home
                }
            }
        })

        navView.setOnNavigationItemSelectedListener {
            val position: Int = when (it.itemId) {
                R.id.navigation_home -> 0
                R.id.navigation_tool -> 1
                R.id.navigation_official -> 2
                R.id.navigation_project -> 3
                else -> 0
            }
            viewPager.setCurrentItem(position, false)

            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun loadData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        GlideApp.get(this).clearMemory()
    }
}
