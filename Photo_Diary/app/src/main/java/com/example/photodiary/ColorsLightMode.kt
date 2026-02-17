package com.example.photodiary

import androidx.compose.ui.graphics.Color


/**
 * Implementation of [AppColors] for light mode theme.
 */
class ColorsLightMode : AppColors {
    override val primary = Color(0xFF2C3A59) // Midnight Blue
    override val primary2 = Color(0xFF7477B9) // Purple

    override val secondary = Color(0xFFF2C6C2) // Pink
    override val secondary2 = Color(0xFFA2E6E4) // Gold
    override val secondary3 = Color(0xFFAEB6C8) // Silver

    override val primaryText = Color(0xFF1F2937) // Greyish Blue
    override val secondaryText = Color(0xFF4B5563) // Grey

    override val border = Color(0xFF1E293B) // Dark Blue
}