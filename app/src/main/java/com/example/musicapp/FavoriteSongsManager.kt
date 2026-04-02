package com.example.musicapp

class FavoriteSongsManager {

    private val favoriteSongs = mutableSetOf<Int>()


    fun addFavorite(songIndex: Int) {
        favoriteSongs.add(songIndex)
    }


    fun removeFavorite(songIndex: Int) {
        favoriteSongs.remove(songIndex)
    }


    fun isFavorite(songIndex: Int): Boolean {
        return favoriteSongs.contains(songIndex)
    }

    fun getFavorites(): Set<Int> {
        return favoriteSongs
    }

    fun clearFavorites() {
        favoriteSongs.clear()
    }
}