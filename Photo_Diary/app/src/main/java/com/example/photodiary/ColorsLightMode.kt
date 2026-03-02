package com.example.photodiary

import androidx.compose.ui.graphics.Color


/**
 * Implementation of [AppColors] for light mode theme.
 */
class ColorsLightMode : AppColors {
    override val cardBackground = Color(0xFF2C3A59) // Midnight Blue
    override val navBackground = Color(0xFFF2C6C2) // Pink

    override val title = Color(0xFF1F2937) // Greyish Blue
    override val secondaryText = Color(0xFFA2E6E4) // Gold
    override val mainText = Color(0xFFAEB6C8) // Silver
    override val placeholderText = Color(0xFF4B5563) // Grey

    override val imageBorder = Color(0xFFE5E7EB) // White
    override val buttonBorder = Color(0xFF1E293B) // Dark Blue
    override val cardBorder = Color(0xFF7477B9) // Purple
}