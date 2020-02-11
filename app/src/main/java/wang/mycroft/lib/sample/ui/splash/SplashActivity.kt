package wang.mycroft.lib.sample.ui.splash

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import permissions.dispatcher.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.component.WebService
import wang.mycroft.lib.sample.ui.main.MainActivity
import java.util.concurrent.TimeUnit

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15日
 * @author: wangqiang
 */
@RuntimePermissions
class SplashActivity : CommonActivity() {

    companion object {
        private const val TIME_SPLASH = 3000
    }

    override fun getResId(): Int {
        return R.layout.activity_splash
    }

    override fun initFields(savedInstanceState: Bundle?) {}

    override fun initViews() {
    }

    override fun loadData() {
        lifecycleScope.launchWhenCreated {
            val startTime = System.currentTimeMillis()
            withContext(Dispatchers.IO) {

                SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
                    ClassicsHeader(context).apply {
                        setEnableLastTime(false)
                    }
                }
                SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                    ClassicsFooter(context)
                }

                WebService.start(this@SplashActivity)
                TimeUnit.MILLISECONDS.sleep(TIME_SPLASH - (System.currentTimeMillis() - startTime))
            }

            goMainPageWithPermissionCheck()
        }
    }

    @NeedsPermission(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun goMainPage() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    @OnPermissionDenied(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onPermissionDenied() {
        finish()
    }

    @OnShowRationale(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun showRationaleForPermissions(request: PermissionRequest) {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.title_all_permission)
            setMessage(R.string.message_all_permission)
            setPositiveButton(android.R.string.ok) { _, _ -> request.proceed() }
            setNegativeButton(android.R.string.cancel) { _, _ -> request.cancel() }
            setCancelable(false)
            show()
        }
    }

    override fun onBackPressed() {
        // ignore
        // super.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}