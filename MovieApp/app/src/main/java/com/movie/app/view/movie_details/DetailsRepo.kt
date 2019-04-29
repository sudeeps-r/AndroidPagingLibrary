package com.movie.app.view.movie_details

import com.movie.app.core.di.module.SchedulersFacade
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.model.MovieDetails
import com.movie.app.core.domain.service.MService
import com.movie.app.core.util.BaseRepo
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class DetailsRepo @Inject constructor(val bookmarkDao: BookmarkDao, val mService: MService,val schedulersFacade: SchedulersFacade):BaseRepo(bookmarkDao,schedulersFacade) {

    fun getMovieDetails( id:String):Observable<MovieDetails>{
        return  mService.searchMovieDetails(id).subscribeOn(schedulersFacade.io()).observeOn(schedulersFacade.ui())
    }
}