package com.example.photodiary

import androidx.compose.ui.graphics.Color


/**
 * Implementation of [AppColors] for dark mode theme.
 */
class ColorsDarkMode : AppColors {
    override val cardBackground = Color(0xFF1E2433) // Deep Blue
    override val navBackground = Color(0xFFBFA4B5) // Pink

    override val title = Color(0xFFE3E6EB) // White
    override val secondaryText = Color(0xFF9AA4B2) // Silver
    override val mainText = Color(0xFF8FA8C9) // Turquoise
    override val placeholderText = Color(0xFF7C8594) // Light Grey

    override val imageBorder = Color(0xFF3A4252) // Light
    override val buttonBorder = Color(0xFF3A4252) // Light
    override val cardBorder = Color(0xFF6E72A8) // Purple
}