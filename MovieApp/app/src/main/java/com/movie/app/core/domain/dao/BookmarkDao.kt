package com.movie.app.core.domain.dao

import androidx.room.*
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun bookmark(movie:BookMark):Completable

    @Delete
    fun deleteMovie(movie:BookMark):Single<Int>

    @Query("SELECT * FROM BookMark WHERE imdbID = :mid")
    fun checkMovie(mid:String):Single<BookMark>

    @Query("SELECT * FROM BookMark WHERE imdbID = :mid")
    fun getBookMark(mid:String):BookMark

    @Query("SELECT * FROM BookMark" )
    fun getAllBookMark():Single<List<BookMark>>
}