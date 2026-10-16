package com.bccard.munki.core.extensions

import androidx.core.graphics.toColorInt

// 색 문자열("#RRGGBB"·"#AARRGGBB") → ColorInt (형식이 다르면 null)
// 예) "#3182F6" → 0xFF3182F6, "#803182F6" → 0x803182F6(반투명), "3182F6"(# 없음) → null
fun String?.toColorIntOrNull(): Int? = this?.let { runCatching { it.toColorInt() }.getOrNull() }