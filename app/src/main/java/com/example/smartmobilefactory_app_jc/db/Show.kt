package com.example.smartmobilefactory_app_jc.db

import androidx.room.*
import com.example.smartmobilefactory_app_jc.data.Genre
import com.example.smartmobilefactory_app_jc.data.Language
import com.example.smartmobilefactory_app_jc.data.ScheduleDay
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity(tableName = "shows")
data class ShowEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "language") val language: Language,
    @ColumnInfo(name = "rating") val rating: Double,
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "show_id") val showId: Long,
    @ColumnInfo(name = "genre") val genre: Genre,
)

@Entity(tableName = "schedule_days")
data class ScheduleDayEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "show_id") val showId: Long,
    @ColumnInfo(name = "schedule_day") val scheduleDay: ScheduleDay,
)

data class ShowWithGenreWithScheduleDay(
    @Embedded val show: ShowEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "show_id",
    )
    val genres: List<GenreEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "show_id",
    )
    val scheduleDays: List<ScheduleDayEntity>,
)

@Dao
interface ShowDao {

    @Transaction
    @Query("SELECT * FROM shows")
    fun getAll(): Single<List<ShowWithGenreWithScheduleDay>>

    @Transaction
    @Query("SELECT * FROM shows where id = :id limit 1")
    fun getById(id: Long): Single<ShowWithGenreWithScheduleDay>

    @Transaction
    @Query("SELECT * FROM shows WHERE name LIKE '%' || :query || '%'")
    fun search(query: String): Single<List<ShowWithGenreWithScheduleDay>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(show: ShowEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: GenreEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scheduleDay: ScheduleDayEntity): Completable

    @Query("DELETE FROM genres WHERE show_id = :showId")
    fun clearGenres(showId: Long): Completable

    @Query("DELETE FROM schedule_days WHERE show_id = :showId")
    fun clearScheduleDay(showId: Long): Completable

}
