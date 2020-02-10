package wang.mycroft.lib.sample.common

import android.app.Dialog
import wang.mycroft.lib.base.BaseFragment
import wang.mycroft.lib.sample.R

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
open class CommonFragment : BaseFragment() {

    override fun createLoadingDialog(): Dialog {
        val dialog = Dialog(context!!, R.style.CommonLoadingDialogStyle)
        dialog.setContentView(R.layout.common_loading_dialog)
        return dialog
    }
}