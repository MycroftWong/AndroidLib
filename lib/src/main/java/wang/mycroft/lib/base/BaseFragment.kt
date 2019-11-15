package wang.mycroft.lib.base

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import wang.mycroft.lib.R
import wang.mycroft.lib.base.LoadingDialogHelper.DialogCreator

/**
 * 添加了 LoadingDialog的 Fragment
 *
 * @author Mycroft Wong
 * @date 2016年6月13日
 */
abstract class BaseFragment : Fragment() {

    private var loadingDialogHelper: LoadingDialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialogHelper =
            LoadingDialogHelper(this, object : DialogCreator {
                override fun createLoadingDialog(): Dialog {
                    return this@BaseFragment.createLoadingDialog()
                }
            })

        view!!.setOnClickListener {
            showLoadingDialog()
        }
    }

    /**
     * 显示通用的加载[Dialog], 默认cancelable=false
     */
    fun showLoadingDialog() {
        loadingDialogHelper?.showLoadingDialog()
    }

    /**
     * 显示通用的加载[Dialog]
     *
     * @param cancelable 是否允许点击空白处取消
     */
    fun showLoadingDialog(cancelable: Boolean) {
        loadingDialogHelper?.showLoadingDialog(cancelable)
    }

    /**
     * 构造通用的加载[Dialog], 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    open fun createLoadingDialog(): Dialog {
        val dialog = Dialog(context!!, R.style.LoadingDialog)
        dialog.setContentView(R.layout.common_dialog)
        return dialog
    }

    /**
     * 隐藏加载Dialog
     */
    fun hideLoadingDialog() {
        loadingDialogHelper?.hideLoadingDialog()
    }
}
