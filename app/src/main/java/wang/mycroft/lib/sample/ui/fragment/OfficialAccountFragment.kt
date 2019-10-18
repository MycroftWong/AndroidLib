package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_official_account.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment
import wang.mycroft.lib.sample.model.OfficialAccount
import wang.mycroft.lib.sample.repository.OfficialAccountRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
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

    private val officialAccountRepository: OfficialAccountRepository by lazy {
        ViewModelProvider(this)[OfficialAccountRepository::class.java]
    }

    private val officialAccountList = ArrayList<OfficialAccount>()

    private var adapter: OfficialAccountAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        officialAccountRepository.officialAccountList.observe(this, officialAccountObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_official_account, container, false)

        holder = Loading.getDefault().wrap(view).withRetry {
            holder.showLoading()
            officialAccountRepository.loadOfficialAccountList()
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
            officialAccountRepository.loadOfficialAccountList()
        }
    }

    private val officialAccountObserver =
        Observer<ResultModel<List<OfficialAccount>>> { resultModel ->
            if (resultModel.errorCode == ResultModel.CODE_SUCCESS) {
                holder.showLoadSuccess()
                officialAccountList.addAll(resultModel.data)
                adapter?.notifyDataSetChanged()
            } else {
                holder.showLoadFailed()
            }
        }
}