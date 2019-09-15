package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayout
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_official_account.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.OfficialAccount
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.ui.adapter.recycler.OfficialAccountAdapter
import wang.mycroft.lib.sample.ui.view.OnTabSelectedAdapter
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder
import java.util.*

/**
 * 公众号
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class OfficialAccountFragment : CommonFragment() {

    companion object {

        fun newInstance(): OfficialAccountFragment {
            val args = Bundle()
            val fragment = OfficialAccountFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var holder: LoadingHolder

    private val officialAccountList = ArrayList<OfficialAccount>()

    private var adapter: OfficialAccountAdapter? = null

    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_official_account, container, false)

        holder = Loading.getDefault().wrap(view).withRetry {
            holder.showLoading()
            loadData()
        }
        return holder.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OfficialAccountAdapter(childFragmentManager, officialAccountList)
        viewPager.adapter = adapter

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(object : OnTabSelectedAdapter() {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.position, false)
                }
            })
        }

        if (officialAccountList.isEmpty()) {
            holder.showLoading()
        } else {
            holder.showLoadSuccess()
        }
    }

    override fun onResume() {
        super.onResume()
        if (officialAccountList.isEmpty()) {
            loadData()
        }
    }

    private fun loadData() {
        if (disposable != null) {
            return
        }

        disposable = NetService.getInstance().officialAccountList
            .subscribe({ officialAccounts ->
                holder.showLoadSuccess()
                officialAccountList.addAll(officialAccounts)
                adapter!!.notifyDataSetChanged()
            }, { throwable ->
                disposable = null
                holder.showLoadFailed()
                ToastUtils.showShort(throwable.message)
            }, { disposable = null })
    }

}