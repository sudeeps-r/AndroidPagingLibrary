package com.movie.app.view.book_mark

import com.movie.app.core.di.module.SchedulersFacade
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.util.BaseRepo
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class BookmarkRepo @Inject constructor(val bookmarkDao: BookmarkDao,val schedulersFacade: SchedulersFacade):BaseRepo(bookmarkDao,schedulersFacade) {


    fun getAllBookMark():Single<List<BookMark>>{
        return bookmarkDao.getAllBookMark().subscribeOn(schedulersFacade.io()).observeOn(schedulersFacade.ui())
    }

}