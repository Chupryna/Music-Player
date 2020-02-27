package com.globallogic.musicplayer.data

import android.content.ContentResolver
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.globallogic.musicplayer.data.model.Audio
import io.reactivex.Single

class AudioRepository {

	companion object {
		const val LIMIT = 20
	}

	private val metadataRetriever = MediaMetadataRetriever()

	fun getAudioFromDevice(contentResolver: ContentResolver, limit: Int, offset: Int): Single<ArrayList<Audio>> {
		return Single.create<ArrayList<Audio>> {
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

			if (cursor != null) {
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
			}

			it.onSuccess(audioList)
		}
	}
}