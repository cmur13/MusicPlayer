package com.example.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast

class MusicPlayer(private val context: Context, private val songs: List<Song>) {
    private var mediaPlayer: MediaPlayer? = null
    private var shuffledSongs = mutableListOf<Song>()
    private var isShuffleEnabled = false
    private var currentSongIndex = 0

    // Play a specific song
    fun playSong(song: Song) {
        stop() // Stop any existing playback
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.path)
            prepare()
            start()
        }

        mediaPlayer?.setOnCompletionListener {
            playNext() // Play the next song when the current one finishes
        }

        Toast.makeText(context, "Playing: ${song.title}", Toast.LENGTH_SHORT).show()
    }

    // Play the next song
    fun playNext() {
        if (isShuffleEnabled && shuffledSongs.isNotEmpty()) {
            // Move to the next song in the shuffled list
            if (currentSongIndex < shuffledSongs.size - 1) {
                currentSongIndex++
                playSong(shuffledSongs[currentSongIndex])
            } else {
                Toast.makeText(context, "End of shuffled playlist", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle regular playlist playback here (optional)
            Toast.makeText(context, "Shuffle mode is off or no songs to play", Toast.LENGTH_SHORT).show()
        }
    }

    // Shuffle the songs and start playback
    fun shuffleAndPlay() {
        if (songs.isNotEmpty()) {
            shuffledSongs = songs.shuffled().toMutableList()
            currentSongIndex = 0
            isShuffleEnabled = true
            playSong(shuffledSongs[currentSongIndex])
            Toast.makeText(context, "Shuffle mode activated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No songs to shuffle", Toast.LENGTH_SHORT).show()
        }
    }

    // Stop playback and release MediaPlayer resources
    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Clean up resources when the player is destroyed
    fun release() {
        stop()
    }
}
