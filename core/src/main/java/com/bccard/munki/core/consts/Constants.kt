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
    const val UNSPLASH_ACCESS_KEY               = "3oL-jHie1qDPHYnV_XSKC8wn4-PI-1VuKUoYY1qew1M"     // Unsplash API Access Key — Authorization: Client-ID {키}, local.properties에 존재함이 맞음
}