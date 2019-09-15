package wang.mycroft.lib.sample.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.blankj.utilcode.util.KeyboardUtils
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_search.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.shared.SearchViewModel

/**
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
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

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        searchViewModel.searchKey.observe(this, Observer<String> { hotKey ->
            search()
            searchEdit.setText(hotKey)
            searchEdit.setSelection(hotKey.length)
        })

        backImage.setOnClickListener {
            if (!findNavController(R.id.nav_host_fragment).navigateUp()) {
                finish()
            }
        }

        searchEdit!!.setOnEditorActionListener { _, i, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == i) {
                val sequence = searchEdit!!.text
                if (!TextUtils.isEmpty(sequence) && TextUtils.getTrimmedLength(sequence) > 0) {
                    searchViewModel.setSearchKey(sequence.toString())
                }
                return@setOnEditorActionListener true
            }

            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun loadData() {
    }

    private fun search() {
        KeyboardUtils.hideSoftInput(searchEdit!!)
        findNavController(R.id.nav_host_fragment).currentDestination?.run {
            if (this.id != R.id.navigation_search_result) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_search_result)
            }
        }
    }
}