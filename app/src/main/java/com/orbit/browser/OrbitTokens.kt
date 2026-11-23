package com.orbit.browser

/**
 * Orbit design tokens — copied from Zephium's design system
 * (`frame/src/styles/tokens.css` and `frame/src/styles/tokens/primitive.css`).
 *
 * Zephium has no accent colour: luminance carries every state. Dark is the
 * base; light mirrors it by luminance. The values below are Zephium's exact
 * token values, adapted to Compose [androidx.compose.ui.graphics.Color].
 *
 * Dark: canvas/page #1A1A1D (gray-850), chrome #1F1F22 (gray-800),
 * surface #26262A (gray-750), raised #2C2C30, float #3D3D44,
 * text #F4F4F6, label-secondary #D8D8DB.
 * Light: page/canvas #F1F1F3, chrome #E6E6E9 (gray-150), surface #FFFFFF,
 * text #1C1C1F, label-secondary #55555C.
 */
object OrbitTokens {
    // Gray ladder (primitive.css)
    const val GRAY_0 = 0xFFFFFFFF
    const val GRAY_50 = 0xFFF9F9FA
    const val GRAY_100 = 0xFFEEEFF1
    const val GRAY_150 = 0xFFE6E6E9
    const val GRAY_200 = 0xFFDADADD
    const val GRAY_300 = 0xFFC3C3C7
    const val GRAY_400 = 0xFF9C9CA1
    const val GRAY_500 = 0xFF717176
    const val GRAY_600 = 0xFF4C4C51
    const val GRAY_700 = 0xFF2E2E33
    const val GRAY_750 = 0xFF26262A
    const val GRAY_800 = 0xFF1F1F22
    const val GRAY_850 = 0xFF1A1A1D
    const val GRAY_900 = 0xFF161619
    const val GRAY_950 = 0xFF101012

    // Dark theme (base)
    const val DARK_CANVAS = GRAY_850
    const val DARK_PAGE = GRAY_850
    const val DARK_CHROME = GRAY_800
    const val DARK_SURFACE = GRAY_750
    const val DARK_RAISED = 0xFF2C2C30
    const val DARK_FLOAT = 0xFF3D3D44
    const val DARK_TEXT = 0xFFF4F4F6
    const val DARK_LABEL_SECONDARY = 0xFFD8D8DB
    const val DARK_MUTED = 0xADF4F4F6 // 68% white
    const val DARK_FAINT = 0x70F4F4F6 // 44% white
    const val DARK_BORDER = 0x12FFFFFF // 7% white
    const val DARK_BORDER_STRONG = 0x21FFFFFF // 13% white
    const val DARK_FILL = 0x0DFFFFFF // 5% white
    const val DARK_FILL_HOVER = 0x14FFFFFF // 8% white
    const val DARK_ROW_ACTIVE = 0x13FFFFFF // 7.5% white
    const val DARK_ADDRESS = 0x11FFFFFF // 6.5% white
    const val DARK_CONTROL = 0x17FFFFFF // 9% white

    // Light theme (mirror)
    const val LIGHT_CANVAS = 0xFFF1F1F3
    const val LIGHT_PAGE = 0xFFF1F1F3
    const val LIGHT_CHROME = GRAY_150
    const val LIGHT_SURFACE = GRAY_0
    const val LIGHT_RAISED = GRAY_0
    const val LIGHT_FLOAT = GRAY_0
    const val LIGHT_TEXT = 0xFF1C1C1F
    const val LIGHT_LABEL_SECONDARY = 0xFF55555C
    const val LIGHT_MUTED = 0x9E1C1C1F // 62% ink
    const val LIGHT_FAINT = 0x6B1C1C1F // 42% ink
    const val LIGHT_BORDER = 0x1218181B // 7% ink
    const val LIGHT_BORDER_STRONG = 0x2118181B // 13% ink
    const val LIGHT_FILL = 0x0D18181B // 5% ink
    const val LIGHT_FILL_HOVER = 0x1418181B // 8% ink
    const val LIGHT_ROW_ACTIVE = 0x1318181B // 7.5% ink
    const val LIGHT_ADDRESS = 0x4DFFFFFF // 30% white lift
    const val LIGHT_CONTROL = 0xFFFFFFFF
}
