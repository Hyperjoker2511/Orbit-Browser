package com.orbit.browser

import java.net.URLEncoder

/**
 * Address-bar input handling for Orbit (Patch 1).
 *
 * Default search engine is Google (the user's choice is still open — see the
 * plan; this is the default, changeable in Settings in a later patch).
 * A bare word becomes a search; anything that looks like an address becomes
 * a URL (https:// is added when the scheme is missing).
 */
object UrlUtils {
    private const val SEARCH_URL = "https://www.google.com/search?q="

    /** The start page shown by a fresh tab (an internal page, not a website). */
    const val START_PAGE = "orbit:start"

    fun isStartPage(url: String): Boolean = url == START_PAGE

    /** True when the input should be treated as an address, not a search. */
    fun looksLikeUrl(input: String): Boolean {
        val text = input.trim()
        if (text.isEmpty()) return false
        // Explicit scheme or localhost.
        if (text.startsWith("http://", ignoreCase = true) ||
            text.startsWith("https://", ignoreCase = true) ||
            text.startsWith("localhost", ignoreCase = true) ||
            text.startsWith("127.0.0.1")
        ) return true
        // No spaces, and either a dot in a plausible host position or a port.
        if (text.contains(' ')) return false
        if (text.contains(':') && text.substringAfter(':').all { it.isDigit() }) return true
        val host = text.substringBefore('/')
        // A dot with something on both sides (example.com, sub.example.co.uk)
        // and no leading/trailing dot.
        if (host.contains('.') && !host.startsWith('.') && !host.endsWith('.')) return true
        return false
    }

    /** Resolve address-bar input to a URL to load (a real URL or a search). */
    fun resolveInput(input: String): String {
        val text = input.trim()
        if (text.isEmpty()) return START_PAGE
        if (!looksLikeUrl(text)) {
            return SEARCH_URL + URLEncoder.encode(text, "UTF-8")
        }
        return if (text.startsWith("http://", ignoreCase = true) ||
            text.startsWith("https://", ignoreCase = true)
        ) {
            text
        } else {
            "https://$text"
        }
    }

    /** A short, readable title for a tab: the host, or the page title when known. */
    fun displayTitle(url: String, pageTitle: String?): String {
        if (isStartPage(url)) return "New tab"
        if (!pageTitle.isNullOrBlank()) return pageTitle
        return try {
            val host = url.substringAfter("://").substringBefore('/').substringBefore(':')
            host.removePrefix("www.")
        } catch (e: Exception) {
            url
        }
    }

    /** The text shown in the address bar for a URL (the start page shows nothing). */
    fun displayUrl(url: String): String = if (isStartPage(url)) "" else url
}
