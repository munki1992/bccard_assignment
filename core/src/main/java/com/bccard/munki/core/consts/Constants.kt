package com.bccard.munki.core.consts

/**
 * Constants
 *
 * @author 정문기
 */
object Constants {
    const val SERVER_LOAD_ITEMS_PER_PAGE        = 20                                                // 페이지당 아이템 개수
    const val SERVER_FIRST_PAGE                 = 1                                                 // 서버 첫 페이지 번호 (Unsplash 페이지는 1부터)
    const val LOAD_MORE_THRESHOLD               = 6                                                 // 목록 끝에서 이만큼 남으면 다음 페이지 요청
    const val DATABASE_NAME                     = "unsplash_explorer.db"                            // 기기에 저장되는 Room DB 파일 이름 (배포 후 바꾸면 기존 데이터가 안 보임)
    const val GALLERY_FOLDER                    = "UnsplashExplorer"                                // 다운로드한 사진을 저장할 갤러리 폴더 (Pictures 아래)
    const val IMAGE_MIME_TYPE                   = "image/jpeg"                                      // 저장하는 사진 파일 형식
    const val TRANSITION_DURATION               = 300                                               // 화면 이동 애니메이션 시간 (ms)
}