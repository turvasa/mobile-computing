package com.example.photodiary

import androidx.compose.ui.graphics.Color


/**
 * Implementation of [AppColors] for dark mode theme.
 */
class ColorsDarkMode : AppColors {
    override val cardBackground = Color(0xFF232A3B) // Deep Blue
    override val weatherBackground = Color(0xFF2A3C63) // Midnight Blue
    override val navBackground = Color(0xFFAA8FA2) // Pink
    override val buttonBackground = Color(0xFF6E72A8) // Pink

    override val title = Color(0xFFD6DBE3) // White
    override val secondaryText = Color(0xFF9CA7B8) // Silver
    override val mainText = Color(0xFFA3B4CC) // Turquoise
    override val placeholderText = Color(0xFF525B6B) // Light Grey
    override val buttonText = Color(0xFFF0F3F8) // Light Grey

    override val buttonBorder = Color(0xFF4A5170) // Light
    override val cardBorder = Color(0xFF5C6094) // Purple
}