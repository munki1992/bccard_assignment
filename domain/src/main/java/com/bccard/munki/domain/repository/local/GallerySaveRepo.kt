package com.bccard.munki.domain.repository.local

/**
 * Gallery Save Repo
 *
 * @author 정문기
 */
interface GallerySaveRepo {

    // 갤러리에 이미지 저장 — 성공 여부
    suspend fun save(url: String, fileName: String): Boolean
}