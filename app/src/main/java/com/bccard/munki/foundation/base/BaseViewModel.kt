package com.bccard.munki.foundation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bccard.munki.R
import com.bccard.munki.core.networks.ApiError
import com.bccard.munki.foundation.base.BaseEffect.NavigateBack
import com.bccard.munki.foundation.base.BaseEffect.ShowToast
import com.bccard.munki.foundation.base.BaseEffect.ShowToastMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Compose Base ViewModel
 *
 * @author 정문기
 */
abstract class BaseViewModel<STATE, EFFECT, INTENT>(initialState: STATE) : ViewModel() {

    // Property --------------------------------------------------------------------------------- //

    // STATE: 화면 상태
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    // EFFECT: 화면 고유 일회성 이벤트 (Channel)
    private val _effect = Channel<EFFECT>()
    val effect: Flow<EFFECT> = _effect.receiveAsFlow()

    // BASE EFFECT: 모든 화면 공통 일회성 이벤트 (뒤로가기·화면 이동·토스트)
    private val _baseEffect = Channel<BaseEffect>()
    val baseEffect: Flow<BaseEffect> = _baseEffect.receiveAsFlow()

    // Intent ----------------------------------------------------------------------------------- //

    // 화면 → ViewModel 진입점 (Route 가 호출)
    fun onEvent(intent: INTENT) = handleIntent(intent)

    // Contract-Intent 처리 (자식 ViewModel 이 구현)
    protected abstract fun handleIntent(event: INTENT)

    // 뒤로가기(툴바·시스템 백) — 기본은 이전 화면. 나가기 확인·경로별 분기 등 다르게 처리할 화면만 override
    open fun onBack() = sendBaseEffect(NavigateBack)

    // State · Effect --------------------------------------------------------------------------- //

    // State 갱신
    protected fun setState(reducer: (STATE) -> STATE) = _state.update(reducer)

    // Effect 발행 (withDelay: 화면 전환 애니메이션과 겹치지 않도록 300ms 지연)
    protected fun sendEffect(withDelay: Boolean = false, builder: () -> EFFECT) {
        viewModelScope.launch {
            if (withDelay)
                delay(300.milliseconds)

            _effect.send(builder())
        }
    }

    // 공통 Effect 발행 (withDelay: 화면 전환 애니메이션과 겹치지 않도록 300ms 지연)
    protected fun sendBaseEffect(effect: BaseEffect, withDelay: Boolean = false) {
        viewModelScope.launch {
            if (withDelay)
                delay(300.milliseconds)

            _baseEffect.send(effect)
        }
    }

    // Error ------------------------------------------------------------------------------------ //

    // 공통 오류 안내 토스트 — 인터넷 없음 / 401(Access Key 없음·오류) / 서버 메시지
    protected fun handleCommonError(error: ApiError) = sendBaseEffect(
        when (error) {
            ApiError.NoInternet     -> ShowToast(R.string.msg_common_network_error)
            ApiError.SessionExpired -> ShowToast(R.string.msg_common_unauthorized)
            is ApiError.Server      -> error.message?.let { ShowToastMessage(it) } ?: ShowToast(R.string.msg_common_error)
        }
    )
}