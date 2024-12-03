package com.example.musicplayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Song(
    val title: String,
    val artist: String,
    val duration: String,
    val path: String, // File path for playing the song
    var isFavorite: Boolean = false // To track favorite status
) : Parcelable


