package wang.mycroft.lib.base

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import wang.mycroft.lib.R

/**
 * 添加了 LoadingDialog的 Fragment
 *
 * @author Mycroft Wong
 * @date 2016年6月13日
 */
abstract class BaseFragment : Fragment() {

    private lateinit var loadingDialogHelper: LoadingDialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialogHelper = LoadingDialogHelper(this) { createLoadingDialog() }
    }

    /**
     * 显示通用的加载[Dialog], 默认cancelable=false
     */
    protected fun showLoadingDialog() {
        loadingDialogHelper.showLoadingDialog()
    }

    /**
     * 显示通用的加载[Dialog]
     *
     * @param cancelable 是否允许点击空白处取消
     */
    protected fun showLoadingDialog(cancelable: Boolean) {
        loadingDialogHelper.showLoadingDialog(cancelable)
    }

    /**
     * 构造通用的加载[Dialog], 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    protected open fun createLoadingDialog(): Dialog {
        val dialog = Dialog(context!!, R.style.LoadingDialog)
        dialog.setContentView(R.layout.common_dialog)
        return dialog
    }

    /**
     * 隐藏加载Dialog
     */
    protected fun hideLoadingDialog() {
        loadingDialogHelper.hideLoadingDialog()
    }
}
