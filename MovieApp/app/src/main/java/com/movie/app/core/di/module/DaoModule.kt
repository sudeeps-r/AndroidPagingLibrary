package com.movie.app.core.di.module

import android.content.Context
import androidx.room.Room
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.dao.MovieDB
import com.movie.app.core.util.Constant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
@Module
class DaoModule {

    @Singleton
    @Provides
    fun provideDB(context: Context):MovieDB{
        return Room.databaseBuilder(context,MovieDB::class.java,Constant.MOVIE_DB).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideBookMarkDao(movieDB: MovieDB):BookmarkDao{
        return movieDB.getBookMarkDao()
    }
}