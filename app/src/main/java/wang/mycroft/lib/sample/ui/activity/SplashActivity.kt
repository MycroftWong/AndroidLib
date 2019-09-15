package wang.mycroft.lib.sample.ui.activity

import android.Manifest
import android.os.Bundle
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.component.WebService
import wang.mycroft.lib.util.DisposableUtil
import java.util.concurrent.TimeUnit

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class SplashActivity : CommonActivity() {

    companion object {

        private const val TIME_SPLASH = 3000
    }

    private var disposable: Disposable? = null

    override fun getResId(): Int {
        return R.layout.activity_splash
    }

    override fun initFields(savedInstanceState: Bundle?) {}

    override fun initViews() {
    }

    override fun loadData() {
        doInit()
    }

    override fun onDestroy() {
        DisposableUtil.dispose(disposable)
        disposable = null
        super.onDestroy()
    }

    private fun doInit() {

        disposable = Observable.just(System.currentTimeMillis())
            .subscribeOn(Schedulers.io())
            .map { startTime ->

                SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
                    ClassicsHeader(
                        context
                    )
                }
                SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                    ClassicsFooter(
                        context
                    )
                }

                WebService.start(this)
                TimeUnit.MILLISECONDS.sleep(TIME_SPLASH - (System.currentTimeMillis() - startTime))
                startTime
            }
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                RxPermissions(this@SplashActivity)
                    .request(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            }
            .subscribe({ granted ->
                if (granted!!) {
                    startActivity(MainActivity.getIntent(this))
                }
                finish()
            }, { finish() })
    }

}