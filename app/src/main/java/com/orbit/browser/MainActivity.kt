package com.orbit.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

/**
 * Orbit entry point (Patch 1).
 *
 * Back retreats one level, TV-style: WebView back first, then close the
 * tab (if there is more than one), then leave the app.
 */
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: BrowserViewModel
    private lateinit var webViewHost: OrbitWebViewHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = BrowserViewModel()
        webViewHost = OrbitWebViewHost(this, viewModel)

        // Open a link the user tapped in another app.
        intent?.data?.toString()?.let { url ->
            if (url.startsWith("http")) {
                viewModel.navigateActive(url)
            }
        }

        setContent {
            val darkTheme by viewModel.darkTheme.collectAsState()
            val host = remember { webViewHost }
            OrbitTheme(darkTheme = darkTheme) {
                BrowserScreen(viewModel = viewModel, webViewHost = host)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val tab = viewModel.activeTab()
                if (tab != null && !UrlUtils.isStartPage(tab.url) && webViewHost.canGoBack(tab)) {
                    webViewHost.goBack(tab)
                } else if ((viewModel.tabs.value.size) > 1 && tab != null) {
                    webViewHost.destroyTab(tab.id)
                    viewModel.closeTab(tab.id)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onDestroy() {
        webViewHost.destroyAll()
        super.onDestroy()
    }
}
