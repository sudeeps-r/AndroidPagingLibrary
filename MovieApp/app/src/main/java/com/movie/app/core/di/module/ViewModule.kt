package com.movie.app.core.di.module

import androidx.lifecycle.ViewModel
import com.movie.app.ViewContainer
import com.movie.app.view.book_mark.BookMarkView
import com.movie.app.view.home.HomeView
import com.movie.app.view.movie_details.Details
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Sudeep SR on 04/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */

@Module
abstract class ActivityModule {


    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun  contributeHome():ViewContainer

}

@Module
abstract class  HomeModule{

    @ContributesAndroidInjector
    abstract fun  contributeHomeFragment(): HomeView

    @ContributesAndroidInjector
    abstract fun contributeBookMarkFragment():BookMarkView

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment():Details

}