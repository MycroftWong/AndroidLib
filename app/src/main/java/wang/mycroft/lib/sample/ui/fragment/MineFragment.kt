package wang.mycroft.lib.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonFragment

/**
 * 我的页面
 *
 * @author Mycroft Wong
 * @date 2019年10月11日
 */
class MineFragment : CommonFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }
}