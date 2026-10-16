package com.bccard.munki.presentation.photo.detail.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bccard.munki.R
import com.bccard.munki.core.extensions.toColorIntOrNull
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoExif
import com.bccard.munki.domain.model.network.photo.PhotoLocation
import com.bccard.munki.domain.model.network.photo.PhotoTag
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.domain.model.network.photo.aspectRatio
import com.bccard.munki.foundation.theme.AppTheme
import java.io.File
import java.text.NumberFormat

/**
 * Photo Detail Content
 *
 * @author 정문기
 */
@Composable
fun PhotoDetailContent(photo: Photo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 사진 — 원본 비율 (너무 길거나 넓으면 잘라서 보여준다)
        val ratio = (photo.aspectRatio() ?: 1f).coerceIn(0.6f, 1.5f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
                .clip(RoundedCornerShape(12.dp))
                .background(photo.color.toColorIntOrNull()?.let { Color(it) } ?: AppTheme.colors.surfaceLight)
        ) {
            // 저해상도 이미지
            AsyncImage(
                model = photo.localImagePath?.let(::File) ?: photo.urls?.small,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // 고해상도 이미지 (crossfade로 페이드인 효과)
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(photo.urls?.full)
                    .crossfade(true)
                    .build(),
                contentDescription = photo.altDescription ?: photo.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }

        // 작가
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = photo.user?.profileImage?.large ?: photo.user?.profileImage?.medium,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.surfaceLight)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = photo.user?.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.textStrong
                )

                photo.user?.username?.let {
                    Text(
                        text = stringResource(R.string.str_photo_detail_username, it),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.textSecondary
                    )
                }
            }
        }

        // 설명 (없으면 대체 텍스트)
        (photo.description ?: photo.altDescription)?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = AppTheme.colors.textPrimary
            )
        }

        // 상세 정보 — 값이 있는 것만
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val numberFormat = NumberFormat.getInstance()

            photo.views?.let { PhotoInfoRow(R.string.str_photo_detail_views, numberFormat.format(it)) }
            photo.downloads?.let { PhotoInfoRow(R.string.str_photo_detail_downloads, numberFormat.format(it)) }
            photo.location?.name?.let { PhotoInfoRow(R.string.str_photo_detail_location, it) }
            photo.exif?.name?.let { PhotoInfoRow(R.string.str_photo_detail_camera, it) }
            photo.tags
                ?.mapNotNull { it.title }
                ?.takeIf { it.isNotEmpty() }
                ?.let { titles -> PhotoInfoRow(R.string.str_photo_detail_tags, titles.joinToString(" ") { "#$it" }) }
        }
    }
}

// Preview -------------------------------------------------------------------------------------- //

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoDetailContentPreview() {
    AppTheme {
        PhotoDetailContent(
            photo = Photo(
                id = "preview",
                width = 4000,
                height = 3000,
                color = "#3C3C47",
                description = "설명",
                user = PhotoUser(name = "정문기", username = "정문기"),
                views = 1234567,
                downloads = 8910,
                location = PhotoLocation(name = "대한민국"),
                exif = PhotoExif(name = "갤럭시"),
                tags = listOf(PhotoTag(title = "테스트"))
            )
        )
    }
}