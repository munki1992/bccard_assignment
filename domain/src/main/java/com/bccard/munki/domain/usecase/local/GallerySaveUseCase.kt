package com.bccard.munki.domain.usecase.local

import com.bccard.munki.domain.repository.local.GallerySaveRepo

/**
 * Gallery Save UseCase
 *
 * @author 정문기
 */
class GallerySaveUseCase(private val repo: GallerySaveRepo) {

    // 갤러리에 이미지 저장 — 성공 여부
    suspend fun save(url: String, fileName: String) = repo.save(url, fileName)
}