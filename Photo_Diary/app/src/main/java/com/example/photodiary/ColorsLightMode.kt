package com.example.photodiary

import androidx.compose.ui.graphics.Color


/**
 * Implementation of [AppColors] for light mode theme.
 */
class ColorsLightMode : AppColors {
    override val cardBackground = Color(0xFF2C3A59) // Midnight Blue
    override val weatherBackground = Color(0xFF2F4B7C) // Midnight Blue
    override val navBackground = Color(0xFFF2C6C2) // Pink
    override val buttonBackground = Color(0xFF475A82) // Pink

    override val title = Color(0xFF1F2937) // Greyish Blue
    override val secondaryText = Color(0xFF6B7A90) // Gold
    override val mainText = Color(0xFFAEB6C8) // Silver
    override val placeholderText = Color(0xFF9AA3B5) // Grey
    override val buttonText = Color(0xFFF0F3F8) // Light Grey

    override val buttonBorder = Color(0xFF6C7A99) // Dark Blue
    override val cardBorder = Color(0xFF7477B9) // Purple
}