package wang.mycroft.lib.sample.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity
import wang.mycroft.lib.sample.model.Article

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class WebViewActivity : CommonActivity() {

    companion object {

        private const val EXTRA_ARTICLE = "article.extra"

        fun getIntent(context: Context, article: Article): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE, article)
            return intent
        }
    }

    private var article: Article? = null

    private var agentWeb: AgentWeb? = null

    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            //do you  work
        }
    }
    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            //do you work
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_web_view
    }

    override fun initFields(savedInstanceState: Bundle?) {
        article = if (savedInstanceState == null) {
            intent.getParcelableExtra(EXTRA_ARTICLE)
        } else {
            savedInstanceState.getParcelable(EXTRA_ARTICLE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_ARTICLE, article)
    }

    override fun initViews() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.colorPrimaryDark)
            statusBarDarkFont(true)
        }

        val titleBar = findViewById<TitleBar>(R.id.titleBar)
//        titleBar.title = Html.fromHtml(article!!.title, Html.FROM_HTML_MODE_COMPACT)
        titleBar.title = HtmlCompat.fromHtml(
            article!!.title,
            HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
        )
        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {
                finish()
            }

            override fun onTitleClick(v: View) {

            }

            override fun onRightClick(v: View) {

            }
        })

        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                container,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorAccent), 2)
            //                .setAgentWebWebSettings(getSettings())
            .setWebViewClient(webViewClient)
            .setWebChromeClient(webChromeClient)
            //                .setPermissionInterceptor(mPermissionInterceptor)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            //                .setAgentWebUIController(new UIController(getActivity()))
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            //                .useMiddlewareWebChrome(getMiddlewareWebChrome())
            //                .useMiddlewareWebClient(getMiddlewareWebClient())
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(article!!.link)

    }

    override fun loadData() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        return if (agentWeb!!.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        agentWeb!!.webLifeCycle.onPause()
        super.onPause()

    }

    override fun onResume() {
        agentWeb!!.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }

}
