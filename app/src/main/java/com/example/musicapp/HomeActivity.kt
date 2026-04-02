package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerViewSongs: RecyclerView
    private lateinit var favoriteSection: TextView
    private lateinit var favoriteRecyclerView: RecyclerView
    private val favoriteSongsManager = FavoriteSongsManager()

    private val songTitles = listOf(
        "Hymn for the Weekend","Believer","Thunder","Paradise","Mi Gente","Ve Maahi","O Re Piya","Diet Mountain Dew","Espresso","Under The Influence "
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewSongs = findViewById(R.id.recyclerView)
        favoriteSection = findViewById(R.id.favoritesSection)
        favoriteRecyclerView = findViewById(R.id.favoriteRecyclerView)

        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        recyclerViewSongs.adapter = SongAdapter(songTitles) { position ->
            openSongScreen(position)
        }

        updateFavoriteSection()
    }

    private fun openSongScreen(position: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("SONG_INDEX", position)
        }
        startActivity(intent)
    }

    private fun updateFavoriteSection() {
        val favoriteSongs = favoriteSongsManager.getFavorites()
        if (favoriteSongs.isNotEmpty()) {
            favoriteSection.visibility = View.VISIBLE
            favoriteRecyclerView.visibility = View.VISIBLE
        } else {
            favoriteSection.visibility = View.GONE
            favoriteRecyclerView.visibility = View.GONE
        }
    }
}