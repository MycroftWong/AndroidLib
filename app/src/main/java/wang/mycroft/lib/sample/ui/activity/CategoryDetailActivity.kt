package wang.mycroft.lib.sample.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_category_detail.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.ui.adapter.pager.CategoryDetailPagerAdapter
import wang.mycroft.lib.sample.ui.view.OnTabSelectedAdapter

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class CategoryDetailActivity : CommonActivity() {

    companion object {

        private const val EXTRA_CATEGORY = "category.extra"

        fun getIntent(context: Context, category: Category): Intent {
            return Intent(context, CategoryDetailActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, category)
            }
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_category_detail
    }

    private lateinit var category: Category

    override fun initFields(savedInstanceState: Bundle?) {
        category = if (savedInstanceState == null) {
            intent.getParcelableExtra(EXTRA_CATEGORY)
        } else {
            savedInstanceState.getParcelable(EXTRA_CATEGORY)!!
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_CATEGORY, category)
    }

    override fun initViews() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = category.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPager.adapter = CategoryDetailPagerAdapter(supportFragmentManager, category)

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(object : OnTabSelectedAdapter() {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.position, false)
                }
            })
        }
    }

    override fun loadData() {
    }

}