package com.orbit.browser

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * One browser tab (Patch 1 — basic tabs).
 * A tab remembers its URL and title; the page itself lives in the tab's
 * WebView (owned by the Activity, see [OrbitWebView]). Switching tabs
 * shows that tab's WebView without reloading it.
 */
data class BrowserTab(val id: String, val url: String, val title: String)

/**
 * Tab + navigation state for Orbit. Holds no Views — the WebViews live in
 * the Activity and report back through the setters below.
 */
class BrowserViewModel {
    private val _tabs = MutableStateFlow<List<BrowserTab>>(emptyList())
    val tabs: StateFlow<List<BrowserTab>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow<String?>(null)
    val activeTabId: StateFlow<String?> = _activeTabId.asStateFlow()

    /** Navigation state of the active tab's WebView. */
    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()
    private val _canGoForward = MutableStateFlow(false)
    val canGoForward: StateFlow<Boolean> = _canGoForward.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    /** Whether the UI is in dark theme (Zephium default is dark). */
    private val _darkTheme = MutableStateFlow(true)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    init {
        newTab()
    }

    fun activeTab(): BrowserTab? {
        val id = _activeTabId.value ?: return null
        return _tabs.value.firstOrNull { it.id == id }
    }

    fun newTab() {
        val tab = BrowserTab(UUID.randomUUID().toString(), UrlUtils.START_PAGE, "New tab")
        _tabs.update { it + tab }
        _activeTabId.value = tab.id
        resetNavigationState()
    }

    fun selectTab(id: String) {
        if (_tabs.value.any { it.id == id }) {
            _activeTabId.value = id
            resetNavigationState()
        }
    }

    fun closeTab(id: String) {
        val list = _tabs.value
        if (list.size <= 1) {
            // Never close the last tab — open a fresh one instead.
            val tab = BrowserTab(UUID.randomUUID().toString(), UrlUtils.START_PAGE, "New tab")
            _tabs.value = listOf(tab)
            _activeTabId.value = tab.id
            resetNavigationState()
            return
        }
        val index = list.indexOfFirst { it.id == id }
        if (index < 0) return
        val remaining = list - list[index]
        _tabs.value = remaining
        if (_activeTabId.value == id) {
            val next = remaining.getOrNull(index)?.id ?: remaining.last().id
            _activeTabId.value = next
            resetNavigationState()
        }
    }

    /** The active tab navigates to a new URL (address bar / link / speed dial). */
    fun navigateActive(url: String) {
        val id = _activeTabId.value ?: return
        _tabs.update { list ->
            list.map { if (it.id == id) it.copy(url = url) else it }
        }
    }

    fun setActiveTabTitle(title: String) {
        val tab = activeTab() ?: return
        val clean = title.ifBlank { UrlUtils.displayTitle(tab.url, null) }
        _tabs.update { list ->
            list.map { if (it.id == tab.id) it.copy(title = clean) else it }
        }
    }

    /** Called by the WebView when the active tab's page changes (links, redirects). */
    fun onPageUrlChanged(url: String) {
        val tab = activeTab() ?: return
        _tabs.update { list ->
            list.map { if (it.id == tab.id) it.copy(url = url) else it }
        }
    }

    fun toggleTheme() {
        _darkTheme.update { !it }
    }

    // Reported by the active tab's WebView.
    fun setNavigationState(canBack: Boolean, canForward: Boolean) {
        _canGoBack.value = canBack
        _canGoForward.value = canForward
    }

    fun setLoading(loading: Boolean, progress: Int) {
        _isLoading.value = loading
        _progress.value = progress
    }

    private fun resetNavigationState() {
        _canGoBack.value = false
        _canGoForward.value = false
        _isLoading.value = false
        _progress.value = 0
    }
}
