package com.bccard.munki.foundation.theme

import androidx.compose.material3.darkColorScheme

/**
 * Dark Color Scheme
 *
 * @author 정문기
 */
val DarkColors = darkColorScheme(
    primary            = C_3A8BFA,                                                                  // 주요 버튼, 활성 상태
    onPrimary          = C_FFFFFF,                                                                  // primary 위 텍스트/아이콘
    primaryContainer   = C_1B3358,                                                                  // 덜 강조된 primary (Tonal Button 등)
    onPrimaryContainer = C_D0DFFB,                                                                  // primaryContainer 위 콘텐츠
    secondary          = C_B0B0B8,                                                                  // 보조 버튼, 선택된 탭, 칩
    onSecondary        = C_17171C,                                                                  // secondary 위 콘텐츠
    background         = C_17171C,                                                                  // 전체 화면 배경 (Scaffold)
    onBackground       = C_E4E4E5,                                                                  // 기본 텍스트
    surface            = C_202027,                                                                  // 카드, 다이얼로그, 바텀시트 배경
    onSurface          = C_E4E4E5,                                                                  // surface 위 텍스트
    surfaceVariant     = C_2C2C35,                                                                  // 섹션 구분 배경, 비활성 입력 필드
    onSurfaceVariant   = C_8E8E99,                                                                  // 보조 설명 텍스트, 비활성 텍스트
    outline            = C_3C3C47,                                                                  // 테두리, Divider
    outlineVariant     = C_5A5A66,                                                                  // 덜 강조된 구분선
    error              = C_FF5A64,                                                                  // 에러 메시지, 경고
    onError            = C_FFFFFF                                                                   // error 위 콘텐츠
)

val DarkAppColors = AppColors(
    textStrong        = C_FFFFFF,
    textPrimary       = C_E4E4E5,
    textBody          = C_B0B0B8,
    textSecondary     = C_8E8E99,
    textDisabled      = C_5A5A66,
    textOnAccent      = C_FFFFFF,
    accent            = C_3A8BFA,
    accentLight       = C_1B3358,
    background        = C_17171C,
    surface           = C_202027,
    surfaceLight      = C_2C2C35,
    outline           = C_3C3C47,
    surfaceDark       = C_101014,
    scrim             = C_000000,
    error             = C_FF5A64
)
