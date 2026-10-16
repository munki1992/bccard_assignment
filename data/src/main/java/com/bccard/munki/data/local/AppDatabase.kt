package com.bccard.munki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bccard.munki.data.local.like.LikedPhotoDao
import com.bccard.munki.data.local.like.LikedPhotoEntity

/**
 * App Database
 *
 * 앱 로컬 DB - Room
 *
 * @author 정문기
 */
@Database(
    entities = [LikedPhotoEntity::class],
    version = 1
//    import androidx.room.AutoMigration
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)
abstract class AppDatabase : RoomDatabase() {

    // 좋아요 테이블 명령
    abstract fun likedPhotoDao(): LikedPhotoDao
}