package wang.mycroft.lib.sample.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.text.HtmlCompat
import com.blankj.utilcode.util.LogUtils
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_article_web_view.*
import wang.mycroft.lib.sample.R
import wang.mycroft.lib.sample.common.CommonActivity

class ArticleWebViewActivity : CommonActivity() {

    companion object {

        private const val EXTRA_TITLE = "title.extra"

        private const val EXTRA_URL = "url.extra"

        fun getIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, ArticleWebViewActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_URL, url)
            }
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_article_web_view
    }

    private lateinit var title: String

    private lateinit var url: String

    override fun initFields(savedInstanceState: Bundle?) {
        window.setFormat(PixelFormat.TRANSLUCENT)

        if (savedInstanceState == null) {
            title = intent.getStringExtra(EXTRA_TITLE)
            url = intent.getStringExtra(EXTRA_URL)
        } else {
            title = savedInstanceState.getString(EXTRA_TITLE)!!
            url = savedInstanceState.getString(EXTRA_URL)!!
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putString(EXTRA_TITLE, title)
            putString(EXTRA_URL, url)
        }
    }

    override fun initViews() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = HtmlCompat.fromHtml(
            title, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
        )

        webView.webViewClient = webViewClient
        webView.webChromeClient = webChromeClient
        webView.loadUrl(url)
    }

    override fun loadData() {
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
            LogUtils.e(url)
            if (url?.startsWith("http://") == true || url?.startsWith("https://") == true) {
                webView?.loadUrl(url)
                return true
            }
            return try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                true
            } catch (throwable: Throwable) {
                // ignore
                false
            }
        }
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(webView: WebView?, percent: Int) {
            progressBar.progress = percent
            if (percent >= 100) {
                progressBar.visibility = View.GONE
            }
        }
    }
}
