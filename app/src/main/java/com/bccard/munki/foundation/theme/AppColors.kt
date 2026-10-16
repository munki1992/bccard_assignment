package com.bccard.munki.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * App Colors
 *
 * @author 정문기
 */
@Immutable
data class AppColors(
    // 텍스트
    val textStrong: Color,                                                                          // 제목·강조 (다이얼로그 제목, 툴바 제목)
    val textPrimary: Color,                                                                         // 본문 진한 텍스트
    val textBody: Color,                                                                            // 일반 텍스트·아이콘
    val textSecondary: Color,                                                                       // 보조 설명·힌트·비활성 텍스트
    val textDisabled: Color,                                                                        // 더 약한 비활성·구분 텍스트
    val textOnAccent: Color,                                                                        // 파랑/회색 배경 위 글자·아이콘 — 다크 모드에서도 흰색

    // 브랜드
    val accent: Color,                                                                              // 주요 버튼, 활성 상태, 링크
    val accentLight: Color,                                                                         // 덜 강조된 accent (비활성 버튼 배경, 강조 배경)

    // 배경·표면
    val background: Color,                                                                          // 화면·카드·시트·다이얼로그 배경
    val surface: Color,                                                                             // 섹션 구분 배경, 탭 영역 배경
    val surfaceLight: Color,                                                                        // 입력칸 배경
    val outline: Color,                                                                             // 테두리·구분선·읽기전용 입력칸 배경
    val surfaceDark: Color,                                                                         // 진한 강조 상자
    val scrim: Color,                                                                               // 시트 뒤 어둡게 (alpha 는 사용처에서)

    // 상태
    val error: Color                                                                                // 오류 메시지, 경고
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

// AppTheme.colors 로 접근 (MaterialTheme.colorScheme 와 같은 형태)
object AppTheme {
    val colors: AppColors
        @Composable @ReadOnlyComposable get() = LocalAppColors.current
}