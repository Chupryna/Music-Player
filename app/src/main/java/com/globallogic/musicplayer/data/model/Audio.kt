package com.globallogic.musicplayer.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Audio(
	val id: Long = 0,
	val path: String = "",
	val name: String = "",
	val album: String = "",
	val artist: String = "",
	val image: ByteArray? = null,
	var index: Int = -1
) : Parcelable {
	constructor(source: Parcel) : this(
		source.readLong(),
		source.readString().orEmpty(),
		source.readString().orEmpty(),
		source.readString().orEmpty(),
		source.readString().orEmpty(),
		source.createByteArray(),
		source.readInt()
	)

	override fun describeContents() = 0
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Audio

		if (id != other.id) return false
		if (path != other.path) return false
		if (name != other.name) return false
		if (album != other.album) return false
		if (artist != other.artist) return false
		if (image != null) {
			if (other.image == null) return false
			if (!image.contentEquals(other.image)) return false
		} else if (other.image != null) return false
		if (index != other.index) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + path.hashCode()
		result = 31 * result + name.hashCode()
		result = 31 * result + album.hashCode()
		result = 31 * result + artist.hashCode()
		result = 31 * result + (image?.contentHashCode() ?: 0)
		result = 31 * result + index
		return result
	}

	companion object : Parceler<Audio> {
		override fun Audio.write(parcel: Parcel, flags: Int) = with(parcel) {
			writeLong(id)
			writeString(path)
			writeString(name)
			writeString(album)
			writeString(artist)
			writeByteArray(image)
			writeInt(index)
		}

		override fun create(parcel: Parcel): Audio = Audio(parcel)

		val Null = Audio()
	}
}
