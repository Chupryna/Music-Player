package com.globallogic.musicplayer.data

import android.content.ContentResolver
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import com.globallogic.musicplayer.data.model.Audio
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AudioRepository(private val database: Database) {

	companion object {
		const val TAG = "AudioRepository"
		const val LIMIT = 20
	}

	private val metadataRetriever = MediaMetadataRetriever()

	fun getAudioFromDevice(contentResolver: ContentResolver, limit: Int, offset: Int): Single<ArrayList<Audio>> {
		return Single.create {
			val audioList = ArrayList<Audio>()

			val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
			val projection = arrayOf(
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ARTIST
			)
			val selection = MediaStore.Audio.Media.IS_MUSIC + "!=?"
			val selectionArgs = arrayOf("0")
			val query = MediaStore.Audio.Media.TITLE + " ASC " + "LIMIT $limit OFFSET $offset"

			val cursor = contentResolver.query(
				uri,
				projection,
				selection,
				selectionArgs,
				query
			)

			if (cursor == null) {
				it.onError(Exception("getAudioFromDevice failed: cursor is null"))
				return@create
			}

			while (cursor.moveToNext()) {
				val audioModel = Audio(
					id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
					path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
					name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
					album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
					artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
					image = metadataRetriever.run {
						setDataSource(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
						embeddedPicture
					}
				)
				audioList.add(audioModel)
			}
			cursor.close()
			it.onSuccess(audioList)
		}
	}

	fun trackFavouriteTracks(): Flowable<List<Audio>> {
		try {
			return database.audioDao.trackAll()
		} catch (e: Exception) {
			Log.e(TAG, "trackFavouriteTracks failed")
		}
		return Flowable.empty()
	}

	fun saveFavouriteTrack(track: Audio): Disposable {
		return Completable.fromAction { database.audioDao.insert(track) }
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ }) { Log.e(TAG, "saveFavouriteTrack failed: ${it.message}") }

		/*return try {
			database.audioDao.insert(track) > 0
		} catch (e: Exception) {
			Log.e(TAG, "saveFavouriteTrack failed: ${e.message}")
			false
		}*/
	}
}