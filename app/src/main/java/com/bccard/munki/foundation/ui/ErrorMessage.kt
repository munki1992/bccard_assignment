package com.bccard.munki.foundation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bccard.munki.R
import com.bccard.munki.core.networks.ApiError
import com.bccard.munki.core.networks.helper.handleError

/**
 * Error Message
 *
 * @author 정문기
 */
@Composable
fun Throwable.toErrorMessage(): String = when (val error = handleError(this)) {
    ApiError.NoInternet     -> stringResource(R.string.msg_common_network_error)
    ApiError.SessionExpired -> stringResource(R.string.msg_common_unauthorized)
    is ApiError.Server      -> error.message ?: stringResource(R.string.msg_common_error)
}