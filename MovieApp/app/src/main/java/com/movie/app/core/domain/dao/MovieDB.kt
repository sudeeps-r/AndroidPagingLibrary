package com.movie.app.core.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.domain.model.Movie

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
@Database(entities = arrayOf(BookMark::class),version = 1)
abstract class MovieDB :RoomDatabase() {

    abstract fun getBookMarkDao():BookmarkDao
}