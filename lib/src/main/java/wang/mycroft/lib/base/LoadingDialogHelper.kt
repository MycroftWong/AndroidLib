package wang.mycroft.lib.base

import android.app.Dialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.Utils

/**
 * 加载dialog的帮助类
 *
 * @author Mycroft Wong
 * @date 2019年11月15日
 */
class LoadingDialogHelper(
    lifecycleOwner: LifecycleOwner, private val dialogCreator: DialogCreator
) : DefaultLifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private var loadingDialog: Dialog? = null

    /**
     * 显示通用的加载[Dialog], 默认cancelable=false
     */
    fun showLoadingDialog() {
        showLoadingDialog(false)
    }

    /**
     * 显示通用的加载[Dialog]
     *
     * @param cancelable 是否允许点击空白处取消
     */
    fun showLoadingDialog(cancelable: Boolean) {
        if (loadingDialog == null) {
            loadingDialog = dialogCreator.createLoadingDialog()
        }
        loadingDialog!!.setCancelable(cancelable)
        Utils.runOnUiThread { loadingDialog!!.show() }
    }

    /**
     * 隐藏加载Dialog
     */
    fun hideLoadingDialog() {
        if (loadingDialog?.isShowing == true) {
            Utils.runOnUiThread { loadingDialog!!.cancel() }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (loadingDialog != null) {
            if (loadingDialog!!.isShowing) {
                loadingDialog!!.cancel()
            }
            loadingDialog = null
        }
    }
}