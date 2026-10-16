package com.bccard.munki.foundation.theme

import androidx.compose.material3.lightColorScheme

/**
 * Light Color Scheme
 *
 * @author 정문기
 */
val LightColors = lightColorScheme(
    primary            = C_3182F6,                                                                  // 주요 버튼, 활성 상태
    onPrimary          = C_FFFFFF,                                                                  // primary 위 텍스트/아이콘
    primaryContainer   = C_D0DFFB,                                                                  // 덜 강조된 primary (Tonal Button 등)
    onPrimaryContainer = C_191F28,                                                                  // primaryContainer 위 콘텐츠
    secondary          = C_4E5968,                                                                  // 보조 버튼, 선택된 탭, 칩
    onSecondary        = C_FFFFFF,                                                                  // secondary 위 콘텐츠
    background         = C_FFFFFF,                                                                  // 전체 화면 배경 (Scaffold)
    onBackground       = C_191F28,                                                                  // 기본 텍스트
    surface            = C_FFFFFF,                                                                  // 카드, 다이얼로그, 바텀시트 배경
    onSurface          = C_191F28,                                                                  // surface 위 텍스트
    surfaceVariant     = C_F2F4F6,                                                                  // 섹션 구분 배경, 비활성 입력 필드
    onSurfaceVariant   = C_8B95A1,                                                                  // 보조 설명 텍스트, 비활성 텍스트
    outline            = C_E5E8EB,                                                                  // 테두리, Divider
    outlineVariant     = C_B0B8C1,                                                                  // 덜 강조된 구분선
    error              = C_F04452,                                                                  // 에러 메시지, 경고
    onError            = C_FFFFFF                                                                   // error 위 콘텐츠
)

val LightAppColors = AppColors(
    textStrong        = C_000000,
    textPrimary       = C_191F28,
    textBody          = C_4E5968,
    textSecondary     = C_8B95A1,
    textDisabled      = C_B0B8C1,
    textOnAccent      = C_FFFFFF,
    accent            = C_3182F6,
    accentLight       = C_D0DFFB,
    background        = C_FFFFFF,
    surface           = C_F2F4F6,
    surfaceLight      = C_F9FAFB,
    outline           = C_E5E8EB,
    surfaceDark       = C_333D4B,
    scrim             = C_000000,
    error             = C_F04452
)