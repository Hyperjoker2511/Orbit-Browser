package com.orbit.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Orbit start page — Zephium's "sky" new tab, adapted for TV (Patch 1).
 *
 * Big time, the date, and speed-dial tiles for a few real sites.
 * The tiles are real links (Google, YouTube, Wikipedia); the user can add
 * their own favourites in a later patch (Patch 5, bookmarks).
 */
data class SpeedDialSite(val name: String, val url: String, val letter: String)

val DEFAULT_SPEED_DIAL = listOf(
    SpeedDialSite("Google", "https://www.google.com", "G"),
    SpeedDialSite("YouTube", "https://www.youtube.com", "Y"),
    SpeedDialSite("Wikipedia", "https://www.wikipedia.org", "W"),
    SpeedDialSite("DuckDuckGo", "https://duckduckgo.com", "D"),
)

@Composable
fun StartPage(
    onOpenSite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val now = Date()
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
    val date = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(now)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(48.dp),
        ) {
            Text(
                text = time,
                fontSize = 84.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = date,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(56.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DEFAULT_SPEED_DIAL.forEach { site ->
                    SpeedDialTile(site = site, onClick = { onOpenSite(site.url) })
                }
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Type an address or search above to start browsing",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SpeedDialTile(site: SpeedDialSite, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Text(
                text = site.letter,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = site.name,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
