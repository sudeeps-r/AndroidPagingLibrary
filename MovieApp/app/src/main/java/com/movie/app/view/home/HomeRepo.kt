package com.movie.app.view.home

import com.movie.app.core.di.module.SchedulersFacade
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.model.Movie
import com.movie.app.core.domain.model.MovieResult
import com.movie.app.core.domain.service.MService
import com.movie.app.core.util.BaseRepo
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class HomeRepo @Inject constructor(val bookmarkDao: BookmarkDao,val mService: MService,val scheduler:SchedulersFacade):BaseRepo(bookmarkDao,scheduler) {


    fun getMovies(query:String,pageNo:Int):Observable<MovieResult>{
        return mService.searchMovies(query,pageNo)
    }


}