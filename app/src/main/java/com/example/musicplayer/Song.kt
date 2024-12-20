package com.example.musicplayer

import android.media.MediaMetadataRetriever
import java.util.concurrent.TimeUnit

data class Song(
    val id: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val artUri: String,
    val title: String,
    val path: String, // File path for playing the song
    var isFavorite: Boolean = false // To track favorite status
)

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

// Function to retrieve image art for notifications
fun getImgArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

// Function to check and update the song position
fun setSongPosition(Increment: Boolean) {
    if (!PlayerActivity.repeat) {
        if (Increment) {
            if (PlayerActivity.musicList.size - 1 == PlayerActivity.songPosition) {
                PlayerActivity.songPosition = 0
            } else {
                ++PlayerActivity.songPosition
            }
        } else {
            if (PlayerActivity.songPosition == 0) {
                PlayerActivity.songPosition = PlayerActivity.musicList.size - 1
            } else {
                --PlayerActivity.songPosition
            }
        }
    }
}


