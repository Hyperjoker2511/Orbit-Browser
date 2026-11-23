# Orbit Browser

A Zephium-inspired, Brave-protected browser for Android TV (Jio).

- **Zephium UI, rebuilt native** — the same design tokens (no accent colour, luminance ladder, Light + Dark), the left rail with vertical tabs, and the "sky" start page. Rebuilt in Jetpack Compose from Zephium's exact token values; the Svelte code does not run on TV.
- **Brave ad blocking (Shields)** — Patch 2: filter lists (EasyList / EasyPrivacy / uBlock), video-ad skipping including YouTube, HTTPS upgrade, per-site toggle.
- **Extensions** — Patch 4: our own built-in system (userscripts + built-in tools). Chrome Web Store extensions cannot run in Android WebView; a full Chromium fork is out of scope.
- **No agents / Work** — not a TV thing. No Brave Rewards / Wallet / VPN / News. No sign-in, no account, no Google Play Services, no ads in v1.

## Method

One patch at a time, like PlayBox TV. Each patch = one build, compile after each, user tests on the TV.

| Patch | What it adds |
|---|---|
| 1 | Core browser + Zephium shell: WebView, address bar + search, back/forward/reload, speed-dial start page, left rail (Light/Dark), basic tabs |
| 2 | Brave Shields: ad/tracker blocking, video-ad skipping (incl. YouTube), HTTPS upgrade, per-site toggle, blocked-count panel |
| 3 | Tabs + Spaces + Sleeping tabs (RAM saver for the 2 GB box) |
| 4 | Extensions (built-in system): userscripts + built-in tools + Extensions screen |
| 5 | Bookmarks, history, downloads manager |
| 6 | Private tabs, voice search, find in page, text size, desktop/mobile toggle |
| 7 | Video: picture-in-picture, Open in PlayBox TV, cast (DLNA) |
| 8 | Settings, themes, polish, Jio submission prep |

## Build

- Kotlin + Jetpack Compose + Android WebView. minSdk 26, targetSdk 36.
- `keystore.properties` (local only, never committed) holds the release signing config; copy from `keystore.properties.example`.
- Tests: `./gradlew testDebugUnitTest --offline --no-daemon`
- Release: `./gradlew assembleRelease --offline -x lintVitalRelease -x lint`

## Licence

MIT. Zephium's design tokens are used as inspiration under its MPL-2.0 licence; the Orbit name and logo are our own.
