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
						System.currentTimeMillis(),
						cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
						cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
						cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
						cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
						metadataRetriever.run {
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