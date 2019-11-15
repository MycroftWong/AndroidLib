package wang.mycroft.lib.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import wang.mycroft.lib.R

/**
 * 1. 添加了LoadingDialog
 * 2. 拆分了onCreate 方法
 *
 * @author Mycroft Wong
 * @date 2015年12月30日
 */
abstract class BaseActivity : AppCompatActivity() {

    private var loadingDialogHelper: LoadingDialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initFields(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(getResId())
        loadingDialogHelper =
            LoadingDialogHelper(this, DialogCreator { this@BaseActivity.createLoadingDialog() })
        initViews()
        loadData()
    }

    /**
     * 获取布局资源id
     *
     * @return layout id
     */
    protected abstract fun getResId(): Int

    /**
     * 进行初始化工作，在super.onCreate(Bundle)之前调用
     *
     * @param savedInstanceState 保存的状态
     */
    protected abstract fun initFields(savedInstanceState: Bundle?)

    /**
     * 初始化view, 在[android.app.Activity.setContentView]之后调用
     */
    protected abstract fun initViews()

    /**
     * 加载数据，在所有初始化完成之后调用
     */
    protected abstract fun loadData()

    /**
     * 显示通用的加载[Dialog], 默认cancelable=false
     */
    protected fun showLoadingDialog() {
        loadingDialogHelper?.showLoadingDialog()
    }

    /**
     * 显示通用的加载[Dialog]
     *
     * @param cancelable 是否允许点击空白处取消
     */
    protected fun showLoadingDialog(cancelable: Boolean) {
        loadingDialogHelper?.showLoadingDialog(cancelable)
    }

    /**
     * 隐藏加载Dialog
     */
    protected fun hideLoadingDialog() {
        loadingDialogHelper?.hideLoadingDialog()
    }

    /**
     * 构造通用的加载[Dialog], 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    protected open fun createLoadingDialog(): Dialog {
        val dialog = Dialog(this, R.style.LoadingDialog)
        dialog.setContentView(R.layout.common_dialog)
        return dialog
    }

}