package com.globallogic.musicplayer.data.model

data class Audio(
    val id: Long = 0,
    val path: String = "",
    val name: String = "",
    val album: String = "",
    val artist: String = ""
) {
    var image: ByteArray? = null
}