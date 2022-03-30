package com.example.smartmobilefactory_app_jc.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity(tableName = "seasons")
data class SeasonEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "show_id") val showId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: Int,
)

@Dao
interface SeasonDao {

    @Query("SELECT * FROM seasons")
    fun getAll(): Single<List<SeasonEntity>>

    @Query("SELECT * FROM seasons where show_id = :showId")
    fun getAllByShowId(showId: Long): Single<List<SeasonEntity>>

    @Query("SELECT COUNT(id) FROM seasons where show_id = :showId")
    fun count(showId: Long): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(seasonEntity: SeasonEntity): Completable

    @Query("DELETE FROM seasons WHERE show_id = :showId")
    fun clearSeasons(showId: Long): Completable

}
