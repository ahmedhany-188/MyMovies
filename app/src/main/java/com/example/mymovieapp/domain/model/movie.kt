package com.example.mymovieapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseDate: String,
    val overview: String?,
    val genres: List<String> = emptyList(),
    val runtime: Int? = null,
    var isFavorite: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Movie
        return id == other.id && isFavorite == other.isFavorite
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}
