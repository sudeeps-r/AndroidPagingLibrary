package com.movie.app.core.di.module

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class SchedulersFacade @Inject constructor(){


    fun io(): Scheduler {
        return Schedulers.io()
    }


    fun computation(): Scheduler {
        return Schedulers.computation();
    }

    /**
     * Main Thread scheduler
     */
    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread();
    }

}