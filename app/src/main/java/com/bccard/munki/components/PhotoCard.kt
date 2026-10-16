package com.bccard.munki.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bccard.munki.R
import com.bccard.munki.core.extensions.toColorIntOrNull
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.foundation.theme.AppTheme
import java.io.File

/**
 * Photo Card
 *
 * @author 정문기
 */
@Composable
fun PhotoCard(
    photo: Photo,
    isLiked: Boolean,
    onClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = AppTheme.colors.surface
    ) {
        Column {
            // 사진 — 오프라인용 파일이 있으면 파일로 (좋아요 목록), 없으면 URL 로
            AsyncImage(
                model = photo.localImagePath?.let(::File) ?: photo.urls?.small,
                contentDescription = photo.altDescription ?: photo.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(photo.color.toColorIntOrNull()?.let { Color(it) } ?: AppTheme.colors.surfaceLight)
            )

            // 작가 + 좋아요
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = photo.user?.profileImage?.medium,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.surfaceLight)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = stringResource(R.string.str_photo_list_author, photo.user?.name.orEmpty()),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textBody,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onLikeClick) {
                    Icon(
                        painter = painterResource(if (isLiked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                        contentDescription = stringResource(R.string.str_photo_list_like),
                        tint = AppTheme.colors.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// Preview -------------------------------------------------------------------------------------- //

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoCardPreview() {
    AppTheme {
        Box(Modifier.width(180.dp)) {
            PhotoCard(
                photo = Photo(id = "preview", color = "#3C3C47", user = PhotoUser(name = "정문기")),
                isLiked = true,
                onClick = {},
                onLikeClick = {}
            )
        }
    }
}