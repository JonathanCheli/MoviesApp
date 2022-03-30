package com.example.smartmobilefactory_app_jc.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        EpisodeEntity::class,
        FavoriteEntity::class,
        GenreEntity::class,
        ScheduleDayEntity::class,
        SeasonEntity::class,
        ShowEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    abstract fun seasonDao(): SeasonDao

    abstract fun episodeDao(): EpisodeDao

    abstract fun favoriteDao(): FavoriteDao

}

class AppDatabaseCreator : RoomDatabase.Callback()
