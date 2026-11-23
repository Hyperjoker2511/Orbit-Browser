package com.orbit.browser

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Orbit's WebView host (Patch 1).
 *
 * One [WebView] per tab, owned by the Activity (created with the Activity
 * context). Switching tabs shows that tab's WebView without reloading it.
 * The WebView reports navigation back to the [BrowserViewModel] so the
 * address bar, the tab titles and the back/forward buttons stay in sync.
 *
 * Patch 1 scope: plain WebView, no ad blocking (that is Patch 2, Shields).
 */
class OrbitWebViewHost(
    private val context: Context,
    private val viewModel: BrowserViewModel,
) {
    private val webViews = mutableMapOf<String, WebView>()

    /** The WebView for a tab, created on first use. */
    @SuppressLint("SetJavaScriptEnabled")
    fun webViewFor(tab: BrowserTab): WebView {
        return webViews.getOrPut(tab.id) {
            WebView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                // Desktop-like viewport on the TV so sites render their full layout.
                settings.userAgentString =
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/120.0 Safari/537.36"
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest,
                    ): Boolean {
                        // Stay inside Orbit for http(s) links.
                        return false
                    }

                    override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                        viewModel.setLoading(true, 0)
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        viewModel.setLoading(false, 100)
                        viewModel.setNavigationState(view.canGoBack(), view.canGoForward())
                        viewModel.onPageUrlChanged(url)
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(view: WebView, title: String) {
                        viewModel.setActiveTabTitle(title)
                    }

                    override fun onProgressChanged(view: WebView, newProgress: Int) {
                        viewModel.setLoading(newProgress < 100, newProgress)
                        viewModel.setNavigationState(view.canGoBack(), view.canGoForward())
                    }
                }
                // Load the tab's current URL (or the start page, which loads nothing).
                if (!UrlUtils.isStartPage(tab.url)) loadUrl(tab.url)
            }
        }
    }

    /** Load a URL into a tab's WebView (address bar / speed dial / search). */
    fun loadUrl(tab: BrowserTab, url: String) {
        val webView = webViews[tab.id] ?: return
        if (UrlUtils.isStartPage(url)) return
        if (webView.url != url) webView.loadUrl(url)
    }

    fun goBack(tab: BrowserTab): Boolean {
        val webView = webViews[tab.id] ?: return false
        return if (webView.canGoBack()) {
            webView.goBack()
            true
        } else false
    }

    fun goForward(tab: BrowserTab): Boolean {
        val webView = webViews[tab.id] ?: return false
        return if (webView.canGoForward()) {
            webView.goForward()
            true
        } else false
    }

    fun reload(tab: BrowserTab) {
        webViews[tab.id]?.reload()
    }

    fun canGoBack(tab: BrowserTab): Boolean = webViews[tab.id]?.canGoBack() == true

    /** Drop a tab's WebView when the tab closes. */
    fun destroyTab(tabId: String) {
        webViews.remove(tabId)?.destroy()
    }

    fun destroyAll() {
        webViews.values.forEach { it.destroy() }
        webViews.clear()
    }
}

/**
 * Shows the active tab's WebView, filling the available space.
 * When the active tab changes, the previous WebView is detached and the
 * new one is attached — no reload, the page stays as it was.
 */
@Composable
fun OrbitWebViewContainer(
    host: OrbitWebViewHost,
    activeTab: BrowserTab,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx -> FrameLayout(ctx) },
        update = { frame ->
            val webView = host.webViewFor(activeTab)
            if (webView.parent !== frame) {
                (webView.parent as? ViewGroup)?.removeView(webView)
                frame.removeAllViews()
                frame.addView(webView)
            }
        },
    )
}
