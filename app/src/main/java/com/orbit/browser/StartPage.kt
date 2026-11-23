package com.orbit.browser

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * Orbit start page — Zephium's "sky" new tab, adapted for TV (Patch 1.1).
 *
 * Zephium's new tab is a night sky, not a flat page: a deep gradient with
 * stars, the time, the date, and a stats row. This draws that sky in
 * Compose (an original gradient + stars, not Zephium's photo).
 *
 * Patch 1.1 fix: the page is scrollable and compact, so nothing is clipped
 * on a short landscape screen (the Patch 1 speed-dial tiles were cut off
 * at the bottom and their labels were invisible).
 *
 * The stats row shows only real values. "Ads blocked" is the live Shields
 * count (0 until Patch 2 wires the blocker in); focus/tasks stats arrive
 * with their features and are not faked here.
 */
data class SpeedDialSite(val name: String, val url: String, val letter: String)

val DEFAULT_SPEED_DIAL = listOf(
    SpeedDialSite("Google", "https://www.google.com", "G"),
    SpeedDialSite("YouTube", "https://www.youtube.com", "Y"),
    SpeedDialSite("Wikipedia", "https://www.wikipedia.org", "W"),
    SpeedDialSite("DuckDuckGo", "https://duckduckgo.com", "D"),
)

/** A night sky: deep indigo at the top fading to Zephium's dark canvas. */
private val SKY_TOP = Color(0xFF0D0D24)
private val SKY_MID = Color(0xFF141430)
private val SKY_BOTTOM = Color(0xFF1A1A1D)

@Composable
fun StartPage(
    blockedCount: Int,
    onOpenSite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val now = Date()
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
    val date = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(now)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(SKY_TOP, SKY_MID, SKY_BOTTOM)),
            ),
    ) {
        // Stars, fixed positions (seeded so they do not jump on recomposition).
        SkyStars(modifier = Modifier.fillMaxSize())
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 48.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = time,
                fontSize = 64.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
            )
            Text(
                text = date,
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.75f),
            )
            Spacer(Modifier.height(20.dp))
            // Stats row (Zephium's new-tab stats) — real values only.
            Row(
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatChip(label = "Ads blocked", value = blockedCount.toString())
            }
            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DEFAULT_SPEED_DIAL.forEach { site ->
                    SpeedDialTile(site = site, onClick = { onOpenSite(site.url) })
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Type an address or search above to start browsing",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun SkyStars(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val random = Random(42)
        val count = 90
        repeat(count) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height * 0.7f
            val radius = random.nextFloat() * 1.6f + 0.6f
            val alpha = random.nextFloat() * 0.5f + 0.25f
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = radius,
                center = Offset(x, y),
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
            .padding(6.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.08f)),
        ) {
            Text(
                text = site.letter,
                fontSize = 34.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = site.name,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.85f),
        )
    }
}
