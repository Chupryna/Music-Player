package com.globallogic.musicplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.data.model.AudioDao

/**
 * @author Anatolii Chupryna.
 */

@Database(version = 1, entities = [Audio::class])
abstract class Database: RoomDatabase() {
	abstract val audioDao: AudioDao
}