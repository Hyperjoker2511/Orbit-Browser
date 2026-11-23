package com.orbit.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Orbit's main screen (Patch 1) — Zephium's layout, rebuilt for TV.
 *
 * A left rail (Zephium's sidebar: vertical tabs, new-tab button, theme)
 * and the main area (address bar + the page). D-pad first: every row and
 * button is focusable, Back retreats one level (see MainActivity).
 */
@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel,
    webViewHost: OrbitWebViewHost,
    modifier: Modifier = Modifier,
) {
    val tabs by viewModel.tabs.collectAsState()
    val activeTabId by viewModel.activeTabId.collectAsState()
    val darkTheme by viewModel.darkTheme.collectAsState()
    val canGoBack by viewModel.canGoBack.collectAsState()
    val canGoForward by viewModel.canGoForward.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val activeTab = tabs.firstOrNull { it.id == activeTabId }

    Row(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // ---- Left rail (Zephium sidebar) ----
        Column(
            modifier = Modifier
                .width(288.dp)
                .fillMaxHeight()
                .background(orbitChromeColor(darkTheme))
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            ) {
                Text(
                    text = "Orbit",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { viewModel.toggleTheme() }) {
                    Icon(
                        imageVector = if (darkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = if (darkTheme) "Switch to light" else "Switch to dark",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            // Vertical tabs (Zephium's TabList).
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(tabs, key = { it.id }) { tab ->
                    TabRow(
                        tab = tab,
                        active = tab.id == activeTabId,
                        darkTheme = darkTheme,
                        onSelect = { viewModel.selectTab(tab.id) },
                        onClose = {
                            webViewHost.destroyTab(tab.id)
                            viewModel.closeTab(tab.id)
                        },
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.newTab() }
                    .padding(horizontal = 12.dp, vertical = 12.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "New tab",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "New tab",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // ---- Main area: address bar + page ----
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            AddressBar(
                activeTab = activeTab,
                canGoBack = canGoBack,
                canGoForward = canGoForward,
                darkTheme = darkTheme,
                onBack = { activeTab?.let { webViewHost.goBack(it) } },
                onForward = { activeTab?.let { webViewHost.goForward(it) } },
                onReload = { activeTab?.let { webViewHost.reload(it) } },
                onNavigate = { input ->
                    val tab = activeTab ?: return@AddressBar
                    val url = UrlUtils.resolveInput(input)
                    viewModel.navigateActive(url)
                    webViewHost.loadUrl(tab, url)
                },
            )
            if (isLoading) {
                LinearProgressIndicator(
                    progress = { (progress.coerceIn(0, 100)) / 100f },
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                )
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (activeTab == null) {
                    StartPage(onOpenSite = { url ->
                        val tab = viewModel.activeTab() ?: return@StartPage
                        viewModel.navigateActive(url)
                        webViewHost.loadUrl(tab, url)
                    })
                } else if (UrlUtils.isStartPage(activeTab.url)) {
                    StartPage(onOpenSite = { url ->
                        viewModel.navigateActive(url)
                        webViewHost.loadUrl(activeTab, url)
                    })
                } else {
                    OrbitWebViewContainer(
                        host = webViewHost,
                        activeTab = activeTab,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
private fun TabRow(
    tab: BrowserTab,
    active: Boolean,
    darkTheme: Boolean,
    onSelect: () -> Unit,
    onClose: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (active) orbitRowActiveColor(darkTheme) else Color.Transparent)
            .clickable(onClick = onSelect)
            .padding(horizontal = 12.dp, vertical = 11.dp),
    ) {
        Text(
            text = tab.title,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onClose, modifier = Modifier.then(Modifier)) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close tab",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AddressBar(
    activeTab: BrowserTab?,
    canGoBack: Boolean,
    canGoForward: Boolean,
    darkTheme: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onReload: () -> Unit,
    onNavigate: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    // Keep the field in sync with the page that is showing.
    LaunchedEffect(activeTab?.url) {
        text = activeTab?.let { UrlUtils.displayUrl(it.url) } ?: ""
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(orbitChromeColor(darkTheme))
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        IconButton(onClick = onBack, enabled = canGoBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        IconButton(onClick = onForward, enabled = canGoForward) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Forward",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        IconButton(onClick = onReload, enabled = activeTab != null && !UrlUtils.isStartPage(activeTab.url)) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Reload",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search or type an address") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { onNavigate(text) }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = orbitAddressColor(darkTheme),
                unfocusedContainerColor = orbitAddressColor(darkTheme),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
        )
    }
}
