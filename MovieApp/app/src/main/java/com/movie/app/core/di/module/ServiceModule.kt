package com.movie.app.core.di.module

import com.movie.app.core.domain.service.MService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
@Module(includes = [HttpModule::class])
class ServiceModule {

    @Provides
    @Singleton
    fun provideMovieSearch(retrofit: Retrofit) = retrofit.create(MService::class.java)
}