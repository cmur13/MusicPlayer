package com.example.musicplayer

import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityMainBinding
import android.net.Uri

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Client ID and Redirect URI stored in strings.xml
    private val clientId by lazy { getString(R.string.spotify_client_id) }
    private val redirectUri by lazy { getString(R.string.spotify_redirect_uri) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button click actions
        binding.shuffleE.setOnClickListener {
            // Show a toast message for now (can later be replaced by actual functionality)
            Toast.makeText(this, "Shuffle clicked", Toast.LENGTH_SHORT).show()
        }
        binding.favoriteE.setOnClickListener {
            Toast.makeText(this, "Favorite clicked", Toast.LENGTH_SHORT).show()
        }
        binding.playlistE.setOnClickListener {
            Toast.makeText(this, "Playlist clicked", Toast.LENGTH_SHORT).show()
        }

        // Add a click listener for Spotify login button
        binding.spotifyLoginButton.setOnClickListener {
            // Start the Spotify login process
            loginToSpotify()
        }
    }

    private fun loginToSpotify() {
        // Create the Spotify Authorization URL
        val authUrl = "https://accounts.spotify.com/authorize?client_id=$clientId&response_type=code&redirect_uri=$redirectUri&scope=user-library-read"

        // Open the URL in a browser or WebView
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
        startActivity(intent)
    }
}

