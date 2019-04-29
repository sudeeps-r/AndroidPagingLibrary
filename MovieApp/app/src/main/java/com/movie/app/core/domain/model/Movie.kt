package com.movie.app.core.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Movie(
        @SerializedName("Poster") val poster: String,
        @SerializedName("Title") val title: String,
        @SerializedName("Type") val type: String,
        @SerializedName("Year") val year: String,
        @SerializedName("imdbID") val imdbID: String
        , @Ignore var pos: Int=0, @Ignore var isBookmarked: Boolean = false
)

data class MovieResult(@SerializedName("Search") var result: List<Movie>, @SerializedName("totalResults") val total: Int, @SerializedName("Response") val response: String, @SerializedName("Error") val error: String)


@Entity(tableName = "BookMark")
data class  BookMark(  val poster: String,
                       val title: String,
                       val type: String,
                       val year: String,
                        @PrimaryKey @ColumnInfo(name = "imdbID") val imdbID: String)