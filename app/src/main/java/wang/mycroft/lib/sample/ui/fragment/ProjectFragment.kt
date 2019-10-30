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
import wang.mycroft.lib.sample.model.Project
import wang.mycroft.lib.sample.repository.ProjectRepository
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.ui.adapter.pager.ProjectPagerAdapter
import wang.mycroft.lib.sample.ui.view.OnTabSelectedAdapter
import wang.mycroft.lib.view.Loading
import wang.mycroft.lib.view.LoadingHolder

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class ProjectFragment : CommonFragment() {

    companion object {

        fun newInstance(): ProjectFragment {
            return ProjectFragment()
        }
    }

    private val projectRepository: ProjectRepository by lazy {
        ViewModelProvider(this)[ProjectRepository::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectRepository.projectList.observe(this, projectListObserver)
    }

    private lateinit var holder: LoadingHolder

    private val projectList = mutableListOf<Project>()

    private var adapter: ProjectPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_official_account, container, false)

        holder = Loading.getDefault().wrap(view).withRetry {
            holder.showLoading()
            projectRepository.loadProjectList()

        }
        return holder.wrapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProjectPagerAdapter(childFragmentManager, projectList)
        viewPager.adapter = adapter

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(object : OnTabSelectedAdapter() {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.position, false)
                }
            })
        }

        if (projectList.isEmpty()) {
            holder.showLoading()
        } else {
            holder.showLoadSuccess()
        }
    }

    override fun onResume() {
        super.onResume()
        if (projectList.isEmpty()) {
            projectRepository.loadProjectList()
        }
    }

    private val projectListObserver = Observer<ResultModel<List<Project>>> { resultModel ->
        if (resultModel.errorCode == ResultModel.CODE_SUCCESS) {
            holder.showLoadSuccess()
            projectList.addAll(resultModel.data)
            adapter?.notifyDataSetChanged()
        } else {
            holder.showLoadFailed()
        }
    }

}