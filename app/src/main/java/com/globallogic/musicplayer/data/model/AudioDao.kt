package com.globallogic.musicplayer.data.model

import androidx.room.*
import io.reactivex.Flowable

/**
 * @author Anatolii Chupryna.
 */

@Dao
abstract class AudioDao {
	
	@Query("SELECT * FROM favourite_tracks WHERE id = :id ORDER BY name")
	abstract fun getById(id: Long): Audio

	@Query("SELECT * FROM favourite_tracks")
	abstract fun trackAll(): Flowable<List<Audio>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(item: Audio): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(list: List<Audio>): List<Long>

	@Update
	abstract fun update(item: Audio): Int

	@Update
	abstract fun update(list: List<Audio>): Int

	@Delete
	abstract fun delete(item: Audio)

	@Query("DELETE FROM favourite_tracks")
	abstract fun deleteAll()
}