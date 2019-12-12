package wang.mycroft.lib.sample.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.blankj.utilcode.util.KeyboardUtils
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_search.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.shared.SearchViewModel

/**
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15日
 * @author: wangqiang
 */
class SearchActivity : CommonActivity() {

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_search
    }

    override fun initFields(savedInstanceState: Bundle?) {}

    private lateinit var searchViewModel: SearchViewModel

    override fun initViews() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.colorPrimaryDark)
            statusBarDarkFont(true)
        }

        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        NavigationUI.setupActionBarWithNavController(
            this, findNavController(R.id.nav_host_fragment)
        )

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        searchViewModel.searchKey.observe(this, Observer<String> { hotKey ->
            search()
            searchActionView?.setQuery(hotKey, false)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun loadData() {
    }

    private var searchActionView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        // TODO 设置 SearchView
        searchActionView = menu?.findItem(R.id.searchAction)?.actionView as? SearchView
        searchActionView?.setOnQueryTextListener(onQueryTextListener)
        searchActionView?.queryHint = getString(R.string.hint_search)

        return true
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrEmpty() && TextUtils.getTrimmedLength(query) > 0) {
                searchViewModel.setSearchKey(query)
            }
            return true
        }
    }

/*
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
*/

    private fun search() {
        KeyboardUtils.hideSoftInput(this)
        findNavController(R.id.nav_host_fragment).currentDestination?.run {
            if (this.id != R.id.navigation_search_result) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_search_result)
            }
        }
    }
}