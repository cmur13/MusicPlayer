import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Playlist
import com.example.musicplayer.Song
import com.example.musicplayer.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private var playlists: MutableList<Playlist>,  // Use a mutable list
    private val onClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    // Method to update the playlist with new songs
    fun updatePlaylist(updatedSongs: List<Song>, playlistName: String) {
        val playlist = playlists.find { it.name == playlistName }

        // Only update the songs if the playlist exists
        playlist?.let {
            it.songs.clear()
            it.songs.addAll(updatedSongs)

            // Find the position of the updated playlist and notify the adapter
            val position = playlists.indexOf(it)
            if (position != -1) {
                notifyItemChanged(position)  // Notify that this specific playlist has changed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    override fun getItemCount() = playlists.size

    inner class PlaylistViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.playlistName.text = playlist.name
            binding.root.setOnClickListener { onClick(playlist) }
        }
    }
}


