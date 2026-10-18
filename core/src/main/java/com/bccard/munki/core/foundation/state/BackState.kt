package com.bccard.munki.core.foundation.state

sealed class BackState {
    data object Double : BackState()                                                                // 두 번 눌러야 종료
    data object Immediate : BackState()                                                             // 한 번에 종료
    data object Disabled : BackState()                                                              // 뒤로가기 막기
    data class Custom(val onBack: () -> Unit) : BackState()                                         // 커스텀 동작 (이동 등)
}