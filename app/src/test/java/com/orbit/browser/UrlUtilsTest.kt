package com.orbit.browser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Patch 1 unit tests — address-bar input handling.
 * A bare word becomes a Google search; an address becomes a URL.
 */
class UrlUtilsTest {
    @Test
    fun `bare word becomes a search`() {
        assertEquals(
            "https://www.google.com/search?q=weather",
            UrlUtils.resolveInput("weather"),
        )
    }

    @Test
    fun `phrase becomes a search`() {
        val resolved = UrlUtils.resolveInput("best android tv")
        assertTrue(resolved.startsWith("https://www.google.com/search?q="))
        assertTrue(resolved.contains("best"))
    }

    @Test
    fun `domain gets https added`() {
        assertEquals("https://example.com", UrlUtils.resolveInput("example.com"))
    }

    @Test
    fun `full https url is kept`() {
        assertEquals(
            "https://www.youtube.com",
            UrlUtils.resolveInput("https://www.youtube.com"),
        )
    }

    @Test
    fun `empty input shows the start page`() {
        assertEquals(UrlUtils.START_PAGE, UrlUtils.resolveInput(""))
        assertEquals(UrlUtils.START_PAGE, UrlUtils.resolveInput("   "))
    }

    @Test
    fun `looksLikeUrl rejects plain words`() {
        assertFalse(UrlUtils.looksLikeUrl("weather"))
        assertFalse(UrlUtils.looksLikeUrl("best android tv"))
    }

    @Test
    fun `looksLikeUrl accepts domains`() {
        assertTrue(UrlUtils.looksLikeUrl("example.com"))
        assertTrue(UrlUtils.looksLikeUrl("sub.example.co.uk"))
        assertTrue(UrlUtils.looksLikeUrl("https://example.com/path"))
    }

    @Test
    fun `displayTitle uses the host when no page title`() {
        assertEquals("example.com", UrlUtils.displayTitle("https://example.com/page", null))
        assertEquals("example.com", UrlUtils.displayTitle("https://www.example.com/", null))
    }

    @Test
    fun `displayTitle for the start page is New tab`() {
        assertEquals("New tab", UrlUtils.displayTitle(UrlUtils.START_PAGE, null))
    }

    @Test
    fun `isStartPage only matches the start page`() {
        assertTrue(UrlUtils.isStartPage(UrlUtils.START_PAGE))
        assertFalse(UrlUtils.isStartPage("https://example.com"))
    }
}
