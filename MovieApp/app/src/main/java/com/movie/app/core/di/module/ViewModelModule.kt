package com.movie.app.core.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movie.app.core.di.scope.ViewModelKey
import com.movie.app.view.book_mark.BookmarkViewModel
import com.movie.app.view.home.HomeViewModel
import com.movie.app.view.movie_details.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModle: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookmarkViewModel::class)
    abstract fun bindBookmarkViewModel(bookmarkViewModel: BookmarkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindMovieDetails(movieDetails: DetailsViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: AppViewModelFactory): ViewModelProvider.Factory

}