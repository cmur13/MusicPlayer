package com.example.musicplayer

data class Playlist(val name: String, val songs: MutableList<Song>)
{
    // Function to add a song to the playlist
    fun addSong(song: Song) {
        songs.add(song)
    }
}

