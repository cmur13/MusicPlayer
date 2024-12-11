package com.example.musicplayer

class PlaylistManager {
    private val playlists = mutableListOf<Playlist>()

    // Function to add a song to a specific playlist by name
    fun addSongToPlaylist(song: Song, playlistName: String) {
        val playlist = playlists.find { it.name == playlistName }
        playlist?.addSong(song) // Add song if the playlist exists
    }

    // Function to get a specific playlist
    fun getPlaylist(name: String): Playlist? {
        return playlists.find { it.name == name }
    }

    // Add a new playlist


    // Get all playlists
    fun getAllPlaylists(): List<Playlist> {
        return playlists
    }
}
