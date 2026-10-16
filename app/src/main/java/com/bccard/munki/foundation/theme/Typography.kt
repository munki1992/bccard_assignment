package com.bccard.munki.foundation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.bccard.munki.R

/**
 * Typography
 *
 * @author 정문기
 */

val Pretendard = FontFamily(
    Font(R.font.pretendard_regular_400,  FontWeight.Normal),
    Font(R.font.pretendard_medium_500,   FontWeight.Medium),
    Font(R.font.pretendard_semibold_600, FontWeight.SemiBold),
    Font(R.font.pretendard_bold_700,     FontWeight.Bold)
)

private const val FONT_BOX = 1.1934f

// 한 줄 높이 = 폰트 박스 + 상하 padding
private fun pretendard(size: Int, weight: FontWeight, padding: Float) = TextStyle(
    fontFamily = Pretendard,
    fontSize = size.sp,
    fontWeight = weight,
    lineHeight = (size * FONT_BOX + padding * 2).sp,
    lineHeightStyle = LineHeightStyle(alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None)
)

// 앱 전체 Typography — 12/14/16/24 · Regular/Medium/Bold
val AppTypography = Typography(
    headlineMedium = pretendard(24, FontWeight.Bold,   4.9f),               // 24 Bold    (38.4sp)
    headlineSmall  = pretendard(24, FontWeight.Medium, 4.9f),               // 24 Medium  (38.4sp)
    titleMedium    = pretendard(16, FontWeight.Bold,   3.25f),              // 16 Bold    (25.6sp)
    titleSmall     = pretendard(16, FontWeight.Medium, 3.25f),              // 16 Medium  (25.6sp)
    bodyLarge      = pretendard(16, FontWeight.Normal, 3.25f),              // 16 Regular (25.6sp)
    bodyMedium     = pretendard(14, FontWeight.Normal, 2.85f),              // 14 Regular (22.4sp, 기본 텍스트)
    bodySmall      = pretendard(12, FontWeight.Normal, 2.44f),              // 12 Regular (19.2sp)
    labelLarge     = pretendard(14, FontWeight.Bold,   3.25f)               // 14 Bold    (23.2sp, 버튼)
)